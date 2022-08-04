# CircleHough
[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)

[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

A Java Program for detecting circles within a given image by using Circle Hough Transform (**CHT**).

It displays and stores to the project */results* directory (automatically created): 
* the gray-scale image (*gray.png*)
* the blurred image (*blur.png*)
* the sobel image (*sobel.png*)
* the image with pixels above threshold (*aboveThreshold.png*)
* the image with detected circles in red (*detectedCircles.png*).

Based on:
* https://en.wikipedia.org/wiki/Circle_Hough_Transform
* https://github.com/danilomodica/CircleFinder

## How-To-Use

1) Open a terminal in the project directory <br>

2) Compile Java source code by running: <br>
``` make ``` <br>

3) Run the Main program by running: <br>
``` java circleHough.Main imagePath [sobelThreshold] [minRadius] [maxRadius] [circleThreshold] ```

    Arguments:
    * imagePath (**required**): path of the image file.
    * sobelThreshold (*optional*): is the threshold for the sobel filter, used to discard useless details . **Default**: 150.
    * minRadius (*optional*): minimum radius of the circles to be detected (in px). **Default**: 10.
    * maxRadius (*optional*): maximum radius of the circles to be detected (in px). **Default**: 100.
    * circles (*optional*): number of detected circles to print. **Default**: 1.

