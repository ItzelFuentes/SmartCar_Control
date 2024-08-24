#ifndef iButton_h
#define iButton_h

#include <Arduino.h>

#define FLAG  -1
#define ANALOG 3
#define EVENTS 6

enum EventTypes {
  All,
  Changed,
  Higher,
  Lower,
  Rising,
  Falling
};

class iButton {
  private:
    byte          _buttons;
    byte          *Pin;
    byte          *Mode;
    byte          *posvar;
    word          *x;
    word          *varState;
    word          *lastvarState;    
    bool          *Inverse;
    bool          *prevState;
    bool          *lastState;
    bool          *readState;
    word          *Debounce;
    unsigned long *lastDebounce;
    typedef void (*Event)();
    Event         *_events;
    void          CallEvent(byte index, EventTypes type);
    word          EventIndex(byte index, EventTypes type);

  public:
    iButton(byte buttons );
    ~iButton();
    void          subscribe(byte index, byte mode, word *var, byte posVar,  byte pin = 0,  bool inverse = false, word deb = 0);
    void          event(byte index, EventTypes type, void (*event)());    
    void          setState(byte index, bool data);
    void          debounce(byte index, word data);
    bool          getState(byte index);
    bool          getStateRaw(void);
    void          loop(void);
};

#endif
