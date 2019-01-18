#include <Stepper.h>

Stepper stepper(32,8,9,10,11);//Initializes pins 8-11 for Stepper control, 32 is the number of steps needed for one revolution of the motor

void setup() {
  Serial.begin(9600); // Sets transfer speed to 9600 bits/s; need to match computers transfer speed or garbage data is received
  stepper.setSpeed (200); // Sets rotations speed to 200
}

void loop() {
  int steps = 0;
  if (Serial.available() > 0) { // only runs if input data is received, otherwise does not run
    steps = Serial.parseInt(); //Takes in an input and turns the motor by that many steps
    stepper.step(steps); // 2048 steps = 1 full rotation
    Serial.println(steps);// Prints steps rotated
  }
}
