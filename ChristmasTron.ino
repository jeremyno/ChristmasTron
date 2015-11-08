#include "FastLED.h"
#include <SPI.h>
#include <SD.h>
#include "LedFunctions.h"

// How many leds in your strip?
#define NUM_LEDS 250


// For led chips like Neopixels, which have a data line, ground, and power, you just
// need to define DATA_PIN.  For led chipsets that are SPI based (four wires - data, clock,
// ground, and power), like the LPD8806 define both DATA_PIN and CLOCK_PIN
#define DATA_PIN 3

// Define the array of leds
CRGB leds[NUM_LEDS];
File f;

void sequence(CRGB back, CRGB front) {
  showColor(back, leds, NUM_LEDS);
  delay(1000);
  countColor(front, leds, NUM_LEDS);
  delay(1000);
}

void setup() {
  FastLED.addLeds<WS2811, DATA_PIN, RGB>(leds, NUM_LEDS);
  boolean started = SD.begin(10);
  while (!started) {
    sequence(CRGB::Red,CRGB::Blue);
  }
  
  f = SD.open("anim.bin");

  while (!f) {
    sequence(CRGB::Green, CRGB::Blue);
    
    f = SD.open("anim.bin");
  }
}

void loop() {
  if (f.seek(0)) {
    animateSDFile(f, leds);
  } else {
    showColor(CRGB::Black,leds,NUM_LEDS);
  }
}
