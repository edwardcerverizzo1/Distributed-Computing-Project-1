class Clock extends Main{

  final int time = 3000;
  static private boolean movieInSession = false;
  static Object endOfMovieCV = new Object(); //eomCondtionVarriable

  Clock(){}


  static public boolean isMovieInSession(){
    return movieInSession;
  }

  private synchronized void startMovie(){
    movieInSession = true;
    System.out.println();
    say("The movie has started");
    System.out.println();
  }
  
  private void waitTillMovieIsOver(){
    try{sleep(time);}   
    catch(Exception e){}
  }

  private synchronized void endMovie(){
      movieInSession = false; // should this be false? or should we wait until the people start leaving?
      System.out.println();
      say("The movie has ended");
      System.out.println();
  }

  private void signalSpeaker(){
    System.out.println();
    say("Speaker will now comeout");
    System.out.println();
    synchronized(endOfMovieCV){
      endOfMovieCV.notify();
    }
  }

  public void run(){
    Thread.currentThread().setName("Clock");
    
    // while(Visitor.peopleStillWaitting())

    while(Visitor.peopleStillWaitting()){
      try{sleep(3000);}   
    catch(Exception e){}
      
      startMovie();
      // Visitor.fullDiagnostics();
      waitTillMovieIsOver();
      endMovie();
      

      signalSpeaker();  
    }
    
   
  }
}

