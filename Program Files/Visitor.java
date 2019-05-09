
/*

let $ indicate that something has already been covered

numPeopleLeft is the number of people who have not seen the movie yet.
inTheater is the current amount of people in the movie.


*/



import java.util.Vector;
import java.lang.Thread;
import java.util.Random;

class Visitor extends Main{

  int id;
  static Vector waittingInLobby = new Vector();
  static int numPeopleLeft = NV, inTheater = 0, numPeopleAlreadyInGroup = 0; 
  int myGroupNumber;

  Random r = new Random();

  static boolean diagnostics = false;

  Visitor(){}

  public static void fullDiagnostics(){
    System.out.println("numPeopleLeft: " + numPeopleLeft);
    System.out.println("inTheater: " + inTheater);
    System.out.println("numPeopleAlreadyInGroup: " + numPeopleAlreadyInGroup);
  } 

  Visitor(int x){
    id = x;
  }

  private void waitInLobby(Object convey){ // do I need to make this synchronized?
    synchronized(waittingInLobby){
      say("I am entering the lobby");
      waittingInLobby.addElement(convey);
    }
    synchronized(convey){
      while(true){
        try{convey.wait(); break;}
        catch(InterruptedException e){ continue; }
      }
    }
    say("I am exiting the lobby and going to entering the theater");
  }

  private synchronized boolean atCapacity(){
    return inTheater >= TC;
  }

  private synchronized void enterTheater(){
      inTheater++;
      say("I am entering the theater");
      if(diagnostics)
        System.out.println("inTheater value " + inTheater);    
  }

  void waitForSpeakertoGiveTalk(){
    synchronized(Speaker.speakerConditionVarriable){
      while(true){
        try{Speaker.speakerConditionVarriable.wait(); break;}
        catch(InterruptedException e){ continue; }
      }
    }
  }

  private boolean willNotSeeAgain(){
    synchronized(Visitor.class){
    if(r.nextInt(4) == 3){
        numPeopleLeft--;
        inTheater--;
        say("I am going to leave");
        if(diagnostics){
          System.out.println("inTheater: " + inTheater);
        }
      return true;
    }
    else{
      say("I am going to watch it again");
      return false;
    }
    }
  }

  static public boolean peopleStillWaittingToSeeFilm(){
    return waittingInLobby.size() > 0;
  }

  private void letSomeoneIn(){
    synchronized(Visitor.class){
      synchronized(waittingInLobby){
        if(peopleStillWaittingToSeeFilm()){
          synchronized(waittingInLobby.get(0)){
            waittingInLobby.get(0).notify();
            waittingInLobby.removeElementAt(0);
          }
          if(true){
            System.out.println("numPeopleLeft: " + numPeopleLeft);
          }
        }
      }
    }
  }

  static public boolean peopleStillWaitting(){
    return numPeopleLeft > 0;
  }

  public  void pickGroup(){
    synchronized(Visitor.class){
      myGroupNumber = numPeopleAlreadyInGroup++ / PT;
      System.out.println("inTheater: " + inTheater);
      System.out.println("numPeopleAlreadyInGroup: " + numPeopleAlreadyInGroup);
    }
    
  }

  public void enterGroup(int myGroupNumber){
      synchronized(Visitor.class){
        say("I am in group" + myGroupNumber);
          if(numPeopleAlreadyInGroup == inTheater){
            numPeopleAlreadyInGroup = 0;
            synchronized(Speaker.speakerConditionVarriable){
              Speaker.speakerConditionVarriable.notify();
              say("notified");
            }
          }
      }
    synchronized(ticketGroups.get(myGroupNumber)){      
      while(true){
        try{ticketGroups.get(myGroupNumber).wait(); break;}
        catch(InterruptedException e){ continue; }
      }
    }

  }

  public void run(){
    Thread.currentThread().setName("Person " + id);
    Object convey = new Object();
    // if movie is in session, visitors wait in lobby.
      //will have to wait in a line(they are supposed to come in one-by-one)
        //use a vector to enque convey objects.
      if(Clock.isMovieInSession() || atCapacity()){
        waitInLobby(convey);
      }
      enterTheater();
      
      
    // use a different object for each visitor (similar to rwcv) $

    // when the previous session ends (signaled by clock) all waitting visitors enter the room IN ORDER and take an avalibe seat $ 

    //If there are no free seats, visitors leave the room and wait to see the next presentation >> I assume the re-enter the line. $

    // when the movie ends, a speaker will give a short talk to the present audiance. 
      // => the present audiance needs to wait for dismissal by the speaker.
        // have the audiance wait on a speakerConditionVarriable.
    while(peopleStillWaitting()){
      //watch movie and then wait for speaker to give talk.
      waitForSpeakertoGiveTalk();

      // Once he is done he will announce to the audience that they are free to leave (use notifyAll). $

      // he would like to give sets of party_tickets for a future movie.

      // the audience will gathe into groups of party tickets size (use one object for each group)
      pickGroup();
      enterGroup(myGroupNumber);

      // the speaker will call each group at once and distribute the tickets

      say("thank you for the ticket");

      // after the speaker will wait for the end of the movie when they will have the chance to give another talk.

      //next the visitors will browse arounf for a while and eventually leave the theater

      // they leave in no specific order, however the traffic in and out of the theater has to be synchronized XXXXXXXX use the door object XXXXXXXX

      //some of the visitors are som impressed with the presentation, they they will want to see it one more time (randomly with a probabilty of 75% to see it again)
      if(willNotSeeAgain()){
        break;
      }
      System.out.println("numPeopleLeft: " + numPeopleLeft);
    }
    

    
    say("I am leaving");
    letSomeoneIn();
    System.out.println("numPeopleLeft: " + numPeopleLeft);

  
    
  }

}