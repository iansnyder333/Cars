package cs1501_p3;
import java.util.Scanner;
import java.io.*;
import java.util.NoSuchElementException;


public class CarsPQ implements CarsPQ_Inter{
    String file;
    private final int MAXAVEBUCKET = 20;

    private int initial = 15;
    private int buckets = 20;
    private int muckets = 50;
    private int size;
    private Node[] carlot;
    private Node[] mmlot;
    private Car[] pricelot;
    private Car[] milelot;


    public CarsPQ(String file){
      carlot = new Node[buckets];
      mmlot = new Node[muckets];
      pricelot = new Car[initial];
      milelot = new Car[initial];
      size=0;

      try(Scanner infile = new Scanner(new File(file));){
        infile.nextLine();
        while(infile.hasNext()){
          Car curr = createCar(infile.nextLine());
          add(curr);
          }
        }
      catch(IOException e){
        e.printStackTrace();
      }
    }


    private Car createCar(String s){
      String[] curr = s.split(":");
      Car newcar = new Car(curr[0],curr[1],curr[2],Integer.parseInt(curr[3]),Integer.parseInt(curr[4]),curr[5]);
      return newcar;
    }


    private void upSize(){
      if(size > buckets*MAXAVEBUCKET) upSizeHash();
      if(size>=initial-1){

        Car[] pricecopy = new Car[initial*2];
        Car[] milecopy = new Car[initial*2];
        for(int i=1; i<=size; i++){

          pricecopy[i]=pricelot[i];
          milecopy[i]=milelot[i];
        }
        pricelot = pricecopy;
        milelot = milecopy;
        initial = initial*2;
        return;
      }
      else return;
    }
    private void upSizeHash(){
      Node[] bigger = new Node[carlot.length * 2];
      buckets = bigger.length;
      for(int i=0; i<carlot.length; i++){
        for(Node head=carlot[i]; head!=null; head=head.next){
          int newh = hash(head.data.getVIN());
          Node pos = new Node(head.data);
          if(bigger[newh]==null) bigger[newh]=pos;
          else{
            pos.next=bigger[newh];
            bigger[newh]=pos;
          }
        }
      }
      carlot = bigger;
    }



  	/**
  	 * Add a new Car to the data structure
  	 * Should throw an `IllegalStateException` if there is already car with the
  	 * same VIN in the datastructure.
  	 *
  	 * @param 	c Car to be added to the data structure
  	 */
  	public void add(Car c) throws IllegalStateException{
      if(contains(c)) throw new IllegalStateException();
      upSize();
      Node curr = new Node(c);
      addtomm(c);
      int ind = hash(c.getVIN());

      if(carlot[ind]==null) carlot[ind]=curr;
      else{
        curr.next=carlot[ind];
        carlot[ind]=curr;
      }
      size++;
      pricelot[size]=c;
      c.pind = size;
      milelot[size]=c;
      c.mind = size;
      swim(size,true);
      swim(size,false);
    }
    private void addtomm(Car x){
      int h = hashC(x.getMake(),x.getModel());
      Node mcurr = new Node(x);
      if(mmlot[h]==null) mmlot[h]=mcurr;
      else{
        mcurr.next=mmlot[h];
        mmlot[h]=mcurr;
      }
    }




    private boolean contains(Car x){
      int h = hash(x.getVIN());
      if(carlot[h]==null) return false;
      for(Node head=carlot[h]; head!=null; head=head.next){
        if(head.data.getVIN().equals(x.getVIN())) return true;
      }
      return false;
    }
    private void swim(int k, boolean flag){
      int n = k;
      while(n>1 && greater(n/2,n,flag))
      {
        swap(n,n/2,flag);
        n=n/2;
      }
    }

    private void sink(int k, boolean flag){
      int n = k;
      while(n*2<=size){
        int j=2*n;
        if(j<size && greater(j,j+1,flag))j++;
        if(!greater(n,j,flag)) break;
        swap(n,j,flag);
        n=j;
      }
    }
    private boolean greater(int n, int k, boolean flag){
      if(flag==true){
        //price
        return pricelot[n].getPrice() > pricelot[k].getPrice();
      }
      else{
        //mileage
        return milelot[n].getMileage() > milelot[k].getMileage();

      }
    }
    private void swap(int n, int k, boolean flag){
      if(flag==true){
        Car c = pricelot[n];
        pricelot[n].setPind(k);
        pricelot[n]=pricelot[k];
        pricelot[k].setPind(n);
        pricelot[k]=c;
      }
      else{
        Car f = milelot[n];
        milelot[n].setMind(k);
        milelot[n]=milelot[k];
        milelot[k].setMind(n);
        milelot[k]=f;
    }
  }





  	/**
  	 * Retrieve a new Car from the data structure
  	 * Should throw a `NoSuchElementException` if there is no car with the
  	 * specified VIN in the datastructure.
  	 *
  	 * @param 	vin VIN number of the car to be updated
  	 */
  	public Car get(String vin) throws NoSuchElementException{
      int h = hash(vin);
      if(carlot[h]==null) throw new NoSuchElementException();
      for(Node head=carlot[h]; head!=null; head=head.next){
        if(head.data.getVIN().equals(vin)) return head.data;
      }
      throw new NoSuchElementException();
    }


    /*
    ALL METHODS BELOW MUST BE lg(n)
    */


  	/**
  	 * Update the price attribute of a given car
  	 * Should throw a `NoSuchElementException` if there is no car with the
  	 * specified VIN in the datastructure.
  	 *
  	 * @param 	vin VIN number of the car to be updated
  	 * @param	newPrice The updated price value
  	 */
  	public void updatePrice(String vin, int newPrice) throws NoSuchElementException{
      int h = hash(vin);
      Node c = carlot[h];
      boolean found = false;
      if(carlot[h]==null) throw new NoSuchElementException();
      for(Node head=carlot[h]; head!=null; head=head.next){
        if(head.data.getVIN().equals(vin)){
           c = head;
          found = true;
          break;
        }
      }
      if(found==false) throw new NoSuchElementException();
      else{
      c.data.setPrice(newPrice);
      int p = c.data.getPind();
      swim(p,true);
      sink(p ,true);
    }
    }

  	/**
  	 * Update the mileage attribute of a given car
  	 * Should throw a `NoSuchElementException` if there is not car with the
  	 * specified VIN in the datastructure.
  	 *
  	 * @param 	vin VIN number of the car to be updated
  	 * @param	newMileage The updated mileage value
  	 */
  	public void updateMileage(String vin, int newMileage) throws NoSuchElementException{
      int h = hash(vin);
      Node c = carlot[h];
      boolean found = false;
      if(carlot[h]==null) throw new NoSuchElementException();
      for(Node head=carlot[h]; head!=null; head=head.next){
        if(head.data.getVIN().equals(vin)){
           c = head;
          found = true;
          break;
        }
      }
      if(found==false) throw new NoSuchElementException();
      else{
      c.data.setMileage(newMileage);
      int m = c.data.getMind();
      swim(m,false);
      sink(m ,false);
    }
    }


  	/**
  	 * Update the color attribute of a given car
  	 * Should throw a `NoSuchElementException` if there is not car with the
  	 * specified VIN in the datastructure.
  	 *
  	 * @param 	vin VIN number of the car to be updated
  	 * @param	newColor The updated color value
  	 */
  	public void updateColor(String vin, String newColor) throws NoSuchElementException{
      Car c = get(vin);
      c.setColor(newColor);
    }
    private void mmrem(Car c){
      int h = hashC(c.getMake(),c.getModel());

      Node start = mmlot[h];
      Node end=start;
      if(start.data.getVIN().equals(c.getVIN())){
        mmlot[h]=start.next;
        return;
      }
      while(end.next!=null && !end.next.data.getVIN().equals(c.getVIN())){
        end=end.next;
      }
      if(end.data.getVIN().equals(c.getVIN())){
        Node temp = end.next;
        end = temp;
      }
      if(end.next==null) return;
      if(end.next.next==null){
        end.next=null;
        return;
      }
      end.next=end.next.next;
      mmlot[h]=start;
      return;
    }

    private Node carlotrem(String vin) throws NoSuchElementException{
      int h = hash(vin);
      Node c = carlot[h];
      mmrem(c.data);
      if(carlot[h]==null) throw new NoSuchElementException();
      Node start = carlot[h];
      Node end=start;
      if(start.data.getVIN().equals(vin)){
        c = start;
        carlot[h]=start.next;
        return c;
      }
      while(end.next!=null && !end.next.data.getVIN().equals(vin)){
        end=end.next;
      }
      if(end.data.getVIN().equals(vin)){
        c=end;
        Node temp = end.next;
        end=temp;
        return c;
      }
      if(end.next==null) throw new NoSuchElementException();
      if(end.next.next==null){
        c = end.next;
        end.next=null;
        return c;
      }
      c=end.next.next;
      end.next= end.next.next;
      carlot[h]=start;
      return c;
    }


  	/**
  	 * Remove a car from the data structure
  	 * Should throw a `NoSuchElementException` if there is not car with the
  	 * specified VIN in the datastructure.
  	 *
  	 * @param 	vin VIN number of the car to be removed
  	 */
  	public void remove(String vin) throws NoSuchElementException{
      Node validate = carlotrem(vin);
      int price = validate.data.getPind();
      int mile = validate.data.getMind();

      if(price==1 || mile==1){
        if(mile!=1){
          delmin(true);
          swap(mile,size,false);
          milelot[size]=null;
          size--;
          swim(mile,false);
          sink(mile,false);
          return;
        }
        else if(price!=1){
          delmin(false);
          swap(price,size,true);
          pricelot[size]=null;
          size--;
          swim(price,true);
          sink(price,true);
          return;
        }
        else{
          delmin(true);
          delmin(false);
          size--;
          return;
        }
      }
      swap(price,size, true);
      swap(mile, size, false);
      pricelot[size]=null;
      milelot[size]=null;

      size--;
      swim(price,true);
      sink(price,true);
      swim(mile,false);
      sink(mile,false);



    }
    private void delmin(boolean flag){
      if(flag==true){
      swap(1,size,true);
      sink(1,true);
      pricelot[size]=null;
    }
    else{
      swap(1,size,false);
      sink(1,false);
      milelot[size]=null;
    }
    }

  	/**
  	 * Get the lowest priced car (across all makes and models)
  	 * Should return `null` if the data structure is empty
  	 *
  	 * @return	Car object representing the lowest priced car
  	 */
  	public Car getLowPrice(){
      if(size!=0) return pricelot[1];
      else return null;
    }

  	/**
  	 * Get the lowest priced car of a given make and model
  	 * Should return `null` if the data structure is empty
  	 *
  	 * @param	make The specified make
  	 * @param	model The specified model
  	 *
  	 * @return	Car object representing the lowest priced car
  	 */
  	public Car getLowPrice(String make, String model){
      if(size==0) return null;
      int h = hashC(make,model);
      Node c = mmlot[h];
      for(Node head=c; head!=null; head=head.next){
        if(head.data.getPrice()<c.data.getPrice()){
          if(head.data.getModel().equals(model)) c = head;
        }
      }

      return c.data;
    }

  	/**
  	 * Get the car with the lowest mileage (across all makes and models)
  	 * Should return `null` if the data structure is empty
  	 *
  	 * @return	Car object representing the lowest mileage car
  	 */
  	public Car getLowMileage(){
      if(size!=0) return milelot[1];
      else return null;
    }


  	/**
  	 * Get the car with the lowest mileage of a given make and model
  	 * Should return `null` if the data structure is empty
  	 *
  	 * @param	make The specified make
  	 * @param	model The specified model
  	 *
  	 * @return	Car object representing the lowest mileage car
  	 */
  	public Car getLowMileage(String make, String model){
      if(size==0) return null;
      int h = hashC(make,model);
      Node c = mmlot[h];
      for(Node head=c; head!=null; head=head.next){
        if(head.data.getMileage()<c.data.getMileage()){
          if(head.data.getModel().equals(model)) c = head;
        }
      }

      return c.data;
    }



    private int hash(String x){
      int sum=0, primeshift=11;
      for(int i=0; i<x.length(); i++){
        sum=sum*primeshift+x.charAt(i);
      }
      return Math.abs(sum%buckets);
    }
    private int hashC(String x, String y){
      String g = x+y;
      int sum=0,primeshift=11;
      for(int i=0; i<g.length(); i++){
        sum=sum*primeshift+g.charAt(i);
      }
      return Math.abs(sum%muckets);
    }
}


class Node{
  Car data;
  Node next;
  boolean flag;
  int pind;
  int mind;
  public Node(Car data){
    this.data=data;
    this.next=null;
    this.flag=true;
  }
}
