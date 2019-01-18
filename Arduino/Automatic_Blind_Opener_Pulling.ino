#include <Stepper.h>

Stepper stepper(32,8,9,10,11); //Initializes pins 8-11 for Stepper control, 32 is the number of steps needed for one revolution of the motor


int length = 120; //Length of blind chain in centimeters (Change this if needed)
int turns = length/1.7; //Motor pulls 1.7cm of chain per turn
int totalTurns = turns; //Total number of turns needed to fully open blinds
bool debug = true; //Debug Flag

void setup() {
  Serial.begin(9600); // Sets transfer speed to 9600 bits/s; default bluetooth speed; otherwise it receives garbage data (learnt through experience)
  stepper.setSpeed (200); // Sets motor rotation speed to 200 (Stepper Motor Maxes out at around 400)
}

void loop() {
  
  int received = 0;
  
  if (Serial.available() > 0) { // Only runs if input data is received, otherwise does not run

    received = Serial.read(); //Stores data received via Bluetooth
	  Serial.println(received); //Debug print
	  Serial.println("Bluetooth Received"); //Debug print
   
  	if (received <= 10 && received >= 1) { //Opens blinds by the amount received from the app via bluetooth
  	  
  	  turns /= 10; //Splits total number of turns needed into 10 steps
      turns *= received; //Mutiplies by desired number of steps
      
      if (!debug) {      
        stepper.step(turns*2048); //Turns by desired number of steps
      } else {
        Serial.println("Turning");
        Serial.println(turns); 
        stepper.step(500); 
      }
      
      turns = length/1.7; //Resets turns to total turns needed
      
  	}
     
  	if (received == 69) { //Activates if snooze is pressed
  	  
  	  Serial.println("Snooze Received"); //Debug print
  		turns /= 2;
  		totalTurns -= turns;
      
  		if (!debug) {
        stepper.step(turns*2048); //Opens blinds by half of the total length remaining
      } else {
        Serial.println("Turning");
        Serial.println(turns); 
        stepper.step(500);
      }
  	}
  	  
  	if (received == 101) { //Activates if alarm is dismissed
  	  
        Serial.println("Dismiss Received"); //Debug print
      	turns = totalTurns;
        if (!debug) {
          stepper.step(turns*2048); //Opens blinds completely   
        } else {
          Serial.println("Turning");
          Serial.println(turns); 
          stepper.step(500); 
        }
        turns = length/1.7;
        stepper.step(turns*-2048); //Unwinds Connected Rope Completely
  	}
    received = 0; //Resets received data
  }
}