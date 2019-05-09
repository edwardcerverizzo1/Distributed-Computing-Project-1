

class Speaker extends Main{

  public static Object speakerConditionVarriable = new Object();

  Speaker(){}

  private void waitTillMovieIsOver(){
    synchronized(Clock.endOfMovieCV){
      while(true){
        try{Clock.endOfMovieCV.wait(); break;}
        catch(InterruptedException e){ continue; }
      }
    }
  }

  private void givePresentationAndOfferTickets(){
    System.out.println();
    say("<Speaker gives presentation>");
    say("Who wants free tickets? Groups of " + PT + " please.");
    System.out.println();
  }

  public void startMovieAgain(){
    Main.clock.start();
  }

  public void waitUntilEveryoneIsGroupedUp(){
    try{sleep(2000);
    }catch(Exception e){}
    // while(true){
    // try{speakerConditionVarriable.wait(); break;}
    // catch(InterruptedException e){ continue; }
    // }
  }

  public void announceEndOfMovie(){
    speakerConditionVarriable.notifyAll();
  }

  public void giveOutTickets(){
    synchronized(Main.ticketGroups){
      for(int i = 0; i < Main.ticketGroups.size(); i++){
        synchronized(Main.ticketGroups.get(i)){
          say("here are your tickets, group" + i);
          Main.ticketGroups.get(i).notifyAll();
        }
      }
    }
  }


  public void run(){

    Thread.currentThread().setName("Speaker");

    while(true){
      waitTillMovieIsOver();
      givePresentationAndOfferTickets();

      // Once he is done he will announce to the audience that they are free to leave (use notifyAll). $

      synchronized(speakerConditionVarriable){
        announceEndOfMovie();
        waitUntilEveryoneIsGroupedUp();
      }

      // he would like to give sets of party_tickets for a future movie.

      // the speaker will call each group at once and distribute the tickets
      giveOutTickets();

      // after the speaker will wait for the end of the movie when they will have the chance to give another talk.

      if(Visitor.peopleStillWaitting()){   
        
        continue;
      }
      else
        break;
    }
  }

}