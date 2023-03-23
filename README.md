# java-projects
Just a place for me to put my personal java code


Still need to fill any of this out

# Eclipse Configuration
## Import formatting
- download format file (.xml)
- In Eclipse, Window > Preferences
- Java > Code Style > Formatter
- click Import...
- select the downloaded format
- Apply

## Setup auto-format
- Window > Preferences
- Java > Editor > Save Actions
- Make sure following are selected:
  - Perform the selected actions on save
    - Format source code
      - Format all lines
    - Organize Imports
    - Additional Action
      - Convert control statement bodies to block
      - Add final modifier to private fields
      - Add final modifier to method parameters
      - Add final modifier to local variables
      - Remove unused imports
      - Add missing '@Override' annotations
      - Add missing '@Override' annotations to implementations of interface methods
      - Add missing '@Deprecated' annotations
      - Remove unnecessary casts
      - Remove redundant semicolons
      - Remove redundant type arguments