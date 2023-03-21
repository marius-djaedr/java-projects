package com.me.turnips.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.me.turnips.constants.CurveType;
import com.me.turnips.constants.DayTime;
import com.me.turnips.dtos.CostCurve;
import com.me.turnips.dtos.DailyCost;
import com.me.turnips.dtos.UserInput;

@Service
public class CurveInputServiceSelenium implements ICurveInputService {
	private static final Logger logger = LoggerFactory.getLogger(CurveInputServiceSelenium.class);

	private final File cacheFile = new File("temp/turnips.cache");

	@Override
	public List<CostCurve> getCurves(final UserInput userInput) {

		final CachePojo cachePojo = new CachePojo();
		cachePojo.id.firstTime = userInput.isFirstTime();
		cachePojo.id.previousPattern = userInput.getPreviousPattern();
		cachePojo.id.input = userInput.getWeekCurve();
		//===================================================================================================
		List<CostCurve> curves = loadFromCache(cachePojo);
		if(curves == null) {
			curves = loadFromSite(cachePojo);
			cachePojo.costCurves = curves;
			try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(cacheFile))) {
				out.writeObject(cachePojo);
			} catch(final IOException e) {
				logger.error("Could not write cache file", e);
				return null;
			}
		}
		return curves;
	}

	private List<CostCurve> loadFromCache(final CachePojo cachePojo) {
		cacheFile.getParentFile().mkdirs();
		try {
			cacheFile.createNewFile();
		} catch(final IOException e) {
			logger.error("Could not create cache file", e);
			return null;
		}
		CachePojo cached;
		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(cacheFile))) {
			cached = (CachePojo) in.readObject();
		} catch(final IOException | ClassNotFoundException | ClassCastException e) {
			logger.error("Could not read cache file and convert to object", e);
			return null;
		}

		if(cachePojo.id.equals(cached.id)) {
			logger.info("Loaded from cache");
			return cached.costCurves;
		}

		return null;
	}

	private List<CostCurve> loadFromSite(final CachePojo cachePojo) {
		System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, "C:/chromedriver.exe");
		final WebDriver driver = new ChromeDriver();
		try {
			final WebDriverWait wait = new WebDriverWait(driver, 5);
			driver.manage().window().maximize();
			driver.get("https://turnipprophet.io/index.html");

			final String yesNo = cachePojo.id.firstTime ? "yes" : "no";
			click(wait, By.xpath("//label[@for='first-time-radio-" + yesNo + "']"));

			click(wait, By.xpath(cachePojo.id.previousPattern.getXpath()));

			//input all of the values
			cachePojo.id.input.forEach((e, v) -> input(wait, By.id(e.htmlId), v));

			Thread.sleep(1000);//TODO wait for table to be done loading instead of this hardcoded wait

			final List<WebElement> trs = driver.findElements(By.xpath("//tbody[@id='output']/tr"));
			final List<CostCurve> curves = trs.stream().map(this::convertRow).filter(Objects::nonNull).collect(Collectors.toList());

			//postprocess
			BigDecimal probSum = BigDecimal.ZERO;
			for(final CostCurve curve : curves) {
				curve.getCostMap().put(DayTime.SUNDAY, DailyCost.singleVal(cachePojo.id.input.get(DayTime.SUNDAY)));
				probSum = probSum.add(curve.getProbability());
			}
			final BigDecimal normalizer = BigDecimal.ONE.setScale(10).divide(probSum.setScale(10), RoundingMode.HALF_UP);
			for(final CostCurve curve : curves) {
				curve.setProbability(curve.getProbability().multiply(normalizer));
			}
			return curves;
		} catch(final InterruptedException e) {
			// TODO Auto-generated catch block
			return null;
		} catch(final Exception e) {
			throw e;
		} finally {
			driver.quit();
		}
	}

	private void click(final WebDriverWait wait, final By by) {
		wait.until(ExpectedConditions.elementToBeClickable(by)).click();
	}

	private void input(final WebDriverWait wait, final By by, final Object input) {
		if(input != null) {
			wait.until(ExpectedConditions.elementToBeClickable(by)).sendKeys(input.toString());
		}
	}

	private CostCurve convertRow(final WebElement tr) {
		logger.info("NEW ROW");
		final List<WebElement> tds = tr.findElements(By.xpath("td"));

		final String name = tds.get(0).getText();
		if("All patterns".equals(name)) {
			return null;
		}
		final CostCurve curve = new CostCurve();
		curve.setCurveType(CurveType.getByName(name));

		int col = 1;
		WebElement probTd = tds.get(col++);
		if(probTd.getAttribute("rowspan") != null) {
			probTd = tds.get(col++);
		}
		curve.setProbability(new BigDecimal(probTd.getText().trim().replace("%", "").replace("<", "")).divide(BigDecimal.valueOf(100)));

		final EnumMap<DayTime, DailyCost> map = new EnumMap<>(DayTime.class);
		for(final DayTime dayTime : DayTime.after(DayTime.SUNDAY)) {
			map.put(dayTime, convertCell(tds.get(col++)));
		}
		curve.setCostMap(map);

		return curve;
	}

	private DailyCost convertCell(final WebElement cell) {
		final String value = cell.getText();
		final String[] split = value.split(" to ");
		final Integer minVal = Integer.parseInt(split[0]);
		Integer maxVal;
		if(split.length == 2) {
			maxVal = Integer.parseInt(split[1]);
		} else {
			maxVal = minVal;
		}
		final DailyCost cost = new DailyCost();
		cost.setMinValue(minVal);
		cost.setMaxValue(maxVal);
		return cost;
	}

}
