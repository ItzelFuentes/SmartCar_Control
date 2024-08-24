#include "iButton.h"

iButton::iButton(byte buttons) {
  _buttons     = buttons;
  Mode         = new byte[_buttons];
  posvar       = new byte[_buttons];
  Pin          = new byte[_buttons];
  Inverse      = new bool[_buttons];
  Debounce     = new word[_buttons];
  lastDebounce = new unsigned long[_buttons];
  lastState    = new bool[_buttons];
  prevState    = new bool[_buttons];
  readState    = new bool[_buttons];
  varState     = new word[_buttons];
  _events      = new Event[EVENTS * _buttons];
  for (word i = 0; i < EVENTS * _buttons; i++)
    _events[i] = nullptr;
}

void iButton::subscribe(byte index, byte mode, word *var, byte posVar, byte pin, bool inverse, word deb) {
  Mode[index]         = mode;
  posvar[index]       = posVar;
  Pin[index]          = pin;
  Inverse[index]      = inverse;
  Debounce[index]     = deb;
  varState[index]     = var;
  if (mode != FLAG) {
    pinMode(Pin[index], mode);
    prevState[index] = digitalRead(Pin[index]);
  }
  else
    prevState[index]  = false;
  if (Inverse[index]) prevState[index] = !prevState[index];
  lastState[index]    = prevState[index];
  readState[index]    = prevState[index];
  lastDebounce[index] = 0;
}

void iButton::event(byte index,  EventTypes type, void (*event)()) {
  _events[EventIndex(index, type)] = event;
}

void iButton::debounce(byte index, word data) {
  Debounce[index] = data;
}

iButton::~iButton() {
  delete[] Mode;
  delete[] posvar;
  delete[] Pin;
  delete[] Inverse;
  delete[] Debounce;
  delete[] lastDebounce;
  delete[] prevState;
  delete[] lastState;
  delete[] readState;
  delete[] _events;
}

void iButton::setState(byte index, bool data) {
  if (Mode[index] == INPUT || Mode[index] == INPUT_PULLUP) return;
  lastState[index] = data;
  x = varState[index];
  word Var = *x;
  bitWrite(Var, posvar[index], data);
  *x = Var;
  CallEvent(index, EventTypes::Changed);
  if (data)
    CallEvent(index, EventTypes::Rising);
  else
    CallEvent(index, EventTypes::Falling);
  if (Inverse[index]) data = !data;
  if (Mode[index] == OUTPUT) digitalWrite(Pin[index], data);
}

bool iButton::getState(byte index) {
  return lastState[index];
}

/*
  bool iButton::getStateRaw(void) {
  lastState = digitalRead(btnPin);
  if (Inverse) lastState = !lastState;
  return lastState;
  }
*/

void iButton::loop(void) {
  unsigned long currentTime;
  for (byte index = 0; index < _buttons; index++) {
    currentTime = millis();
    CallEvent(index, EventTypes::All);
    x = varState[index];
    word Var = *x;
    if (Mode[index] == FLAG || Mode[index] == OUTPUT) {
      bool data = bitRead(Var, posvar[index]);
      if (Debounce[index] != 0 && lastDebounce[index] != 0 && (currentTime - lastDebounce[index]) >= Debounce[index]) {
        data = 0;
        setState(index, data);
        lastDebounce[index] = 0;
      }
      if (data != lastState[index]) {
        if (data)
          lastDebounce[index] = currentTime;
        else
          lastDebounce[index] = 0;
        setState(index, data);
      }
      if (data)
        CallEvent(index, EventTypes::Higher);
      else
        CallEvent(index, EventTypes::Lower);
    }
    if (Mode[index] == INPUT || Mode[index] == INPUT_PULLUP) {
      bool data = digitalRead(Pin[index]);
      if (Inverse[index]) data = !data;
      if (data)
        CallEvent(index, EventTypes::Higher);
      else
        CallEvent(index, EventTypes::Lower);
      if (data != readState[index]) {
        readState[index] = data;
        lastDebounce[index] = currentTime;
      }
      if (readState[index] || lastState[index]) {
        if ((currentTime - lastDebounce[index]) >= Debounce[index]) {
          prevState[index] = lastState[index];
          lastState[index] = readState[index];
          bitWrite(Var, posvar[index], readState[index]);
          *x = Var;
          CallEvent(index, EventTypes::Changed);
          if (readState[index])
            CallEvent(index, EventTypes::Rising);
          else
            CallEvent(index, EventTypes::Falling);
        }
      }
    }
  }
}

void iButton::CallEvent(byte index, EventTypes type) {
  word i = EventIndex(index, type);
  if (_events[i] != nullptr)
    _events[i]();
}

word iButton::EventIndex(byte index, EventTypes type) {
  return index + type * _buttons;
}
