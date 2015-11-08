#include "Arduino.h"
#include <SD.h>
#include "FastLED.h"


/**
 * Reads the frames from frames, format is NUM_LEDS+1, NUM_LEDS(3) colors, RGB; time in 100ths of a second
 */
void animateSDFile(File& file, struct CRGB* leds);

void showColor(CRGB color, struct CRGB* leds, byte len);
void countColor(CRGB color, struct CRGB* leds, byte len);
