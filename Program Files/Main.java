


import java.util.Vector;
import java.lang.Thread;


class Main extends Thread{
  
  static final int NV = 17; // numVisitors
  static final int TC = 8; // theaterCapacity
  static final int PT = 3; // partyTicket

  Object door = new Object();
  
  static Clock clock = new Clock();
  static Speaker speaker = new Speaker();

  static Vector<Object> ticketGroups = new Vector();


  private static void initializeTicketGroups(){
    int numOfGroups = (TC % PT == 0) ? TC / PT : ((TC / PT) + 1);
    for(int i = 0; i < numOfGroups; i++){
      ticketGroups.addElement(new Object());
    }
  }

  public void say(String x){
    System.out.println("[" + System.currentTimeMillis() + "]  " + Thread.currentThread().getName()  + ": " + x);
  }

  public static void main(String[] args) {
    
    initializeTicketGroups();

    speaker.start();

    Visitor[] v = new Visitor[NV];
    for(int i = 0; i < NV; i++){
      v[i] = new Visitor(i);
    }
    for(int i = 0; i < NV; i++){
      v[i].start();
    }

    clock.start();
  }
}