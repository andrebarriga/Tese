#include <ctype.h>
#include <multi_channel_relay.h>
#include <Wire.h>

Multi_Channel_Relay relay;
Multi_Channel_Relay relay2;

bool Initiate = true;
bool Load = false;
bool Go = false;
bool define_number_runs = false;

int L1 = 21;
int P1 = 1;
int DA = 2;
int P2 = 3;
int L2 = 22;
int L3 = 23;
int indice = 0;
int arraywidth;
int number_runs;
int *Sensores;
double *Timers;

void setup() {

  Serial.begin(9600);
  Wire.begin();

  uint8_t adress = relay.scanI2CDevice(1);
  uint8_t adress2 = relay2.scanI2CDevice(adress + 1);
  relay.changeI2CAddress(adress, 0x11);
  relay2.changeI2CAddress(adress2, 0x12);

}

void loop() {

  String readString;
  String Word;
  bool Create_Array = false;

  while (Serial.available() > 0) {

    char c = Serial.read();
    readString += c;
    delay(2);

    if (readString == "Load" && Go == false) {
      relay.channelCtrl(0);
      relay2.channelCtrl(0);
      readString = "";
      Serial.println("LOAD");
      Create_Array = true;
      indice = 0;
      Load = true;
      Go = false;

    }
    else if (readString == "Go") {
      relay.channelCtrl(0);
      relay2.channelCtrl(0);
      readString = "";
      Serial.println("GO");
      define_number_runs = true;
      indice = 0;
      Load = false;
      Go = true;

    }
    else if (readString == "Stop"){
      relay.channelCtrl(0);
      relay2.channelCtrl(0);
      Serial.println("Stop");
      Go = false;
    }

    if (Load) {
      if (Create_Array) {
        if (c == ' ') {
          arraywidth = Word.toInt();
          Sensores = (int*)malloc((int)(arraywidth * sizeof(int)));
          Timers = (double*)malloc((int)(arraywidth * sizeof(double)));
          Create_Array = false;
          readString = "";
        }
      }
      else if (c == '*') {
        Load = false;
        readString = "";
      }
      else if (c == ' ') {
        if (isDigit(readString.charAt(0))) {
          Timers[indice] = Word.toDouble();
          readString = "";
        }
        else {
          if (Word == "L1") {
            Sensores[indice] = L1;
            readString = "";
          }
          else if (Word == "L2") {
            Sensores[indice] = L2;
            readString = "";
          }
          else if (Word == "L3") {
            Sensores[indice] = L3;
            readString = "";
          }
          else if (Word == "P1") {
            Sensores[indice] = P1;
            readString = "";
          }
          else if (Word == "P2") {
            Sensores[indice] = P2;
            readString = "";
          }
          else if (Word == "DA") {
            Sensores[indice] = DA;
            readString = "";
          }
          indice++;
        }
      }
    }
    else if (define_number_runs){
      if (c == ' '){
        define_number_runs = false;
        number_runs = Word.toInt();
        readString = "";        
      }
    }
    Word = readString;
  }
  
  if (Go) {
    if (number_runs > 0){
      int k;
      delay(Timers[indice]*1000);
      if (Sensores[indice] > 20) {
        k = relay.getChannelState() >> (Sensores[indice] - 20 - 1);
        if (k & 1) {
          relay.turn_off_channel((Sensores[indice]) - 20);
        }
        else {
          relay.turn_on_channel((Sensores[indice]) - 20);
        }
      }
      else {
        k = relay2.getChannelState() >> (Sensores[indice] - 1);
        if (k & 1) {
          relay2.turn_off_channel(Sensores[indice]);
        }
        else {
          relay2.turn_on_channel(Sensores[indice]);
        }
      }
      indice++;
  
      if (indice == arraywidth){
        number_runs--;
        indice = 0;
      }
    }

    else
      Go = false;
  }
}
