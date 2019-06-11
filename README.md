# WoodBlockToys-back
### JAVA formation final exam


This is the final exam from my JAVA formation.

We had to create a REST API with Spring to generate barrels of wood blocks.

## REST API

**The API must allow the user to :**

* create an account
* generate a barrel
* order a generated barrel

## Block generation

**Wood blocks were randomly generated with those parameters :**

* shape (square, triangular, hex, rectangular, cylindrical)
* volume (between 10 and 30 cm3)
* height (1, 4, 7 cm)
* wood essence type (oak, pine, beech, mahogany)
* finish (raw, satiny, matte, shiny)
* color (blue, white, red, yellow, orange, green, black)

**special rules :** 

* if the block finish is raw, the block has no color
* if the block has a color, the wood essence type is pine
* materials (wood essence) have to be stored in database


## Barrel generation

**Barrels were randomly generated with those constraints :**

* User's wished maximum barrel price
* User's wished minimum number of pieces in his barrel

