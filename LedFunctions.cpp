#include "LedFunctions.h"

///**
// * Reads the frames from frames, format is NUM_LEDS+1, NUM_LEDS(3) colors, RGB; time in 100ths of a second
// */
//void animateProgMemArray(const uint8_t* frames, int len) {
//  for (int i = 0; i < len; i += (NUM_LEDS * 3 + 1)) {
//    for(unsigned int k = 0; k < NUM_LEDS; k++) {
//      unsigned int l=k*3;
//
//      uint8_t red = pgm_read_byte_near(frames + i + l);
//      uint8_t green = pgm_read_byte_near(frames + i + l + 1);
//      uint8_t blue = pgm_read_byte_near(frames + i + l + 2);
//
//      CRGB color(red, green, blue);
//      leds[k] = color;
//    }
//
//    FastLED.show();
//
//    uint8_t csecs = pgm_read_byte_near(frames + i + NUM_LEDS * 3);
//    unsigned long tdelay = 10 * csecs;
//    delay(tdelay);
//  }
//}

/**
 * Reads the frames from frames, format is NUM_LEDS+1, NUM_LEDS(3) colors, RGB; time in 100ths of a second
 */
void animateSDFile(File& file, CRGB* leds) {
  byte len = file.read();

  while (file.available()) {
    for(unsigned int k = 0; k < len; k++) {
      uint8_t red = file.read();
      uint8_t green = file.read();
      uint8_t blue = file.read();

      CRGB color(red, green, blue);
      leds[k] = color;
    }

    FastLED.show();

    uint8_t csecs = file.read();

    unsigned long tdelay = 10 * csecs;
    delay(tdelay);
  }
}

void showColor(CRGB color, struct CRGB* leds, byte len) {
  for (int i = 0; i < len; i++) {
    leds[i] = color;
  }

  FastLED.show();
}

void countColor(CRGB color, struct CRGB* leds, byte len) {
  for (int i = 0; i < len; i++) {
    // Turn the LED on, then pause
    leds[i] = color;
    FastLED.show();
    delay(200);
  }
}
