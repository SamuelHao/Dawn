void setup() {
  Serial.begin(9600); // Sets transfer speed to 9600 bits/s; (if another baud rate is selected, garbage data is received from the bluetooth receiver)
}

void loop() {
    
  int received = 0;
  
  if (Serial.available() > 0) { // only runs if input data is received, otherwise does not run
  
    received = Serial.read();
    Serial.println("Received Bluetooth Data"); 
    Serial.println(received); //Prints received data
  }
}
