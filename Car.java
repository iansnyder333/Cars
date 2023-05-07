package cs1501_p3;

public class Car implements Car_Inter{

  String vin;
  String make;
  String model;
  int price;
  int mileage;
  String color;

  public int pind;
  public int mind;

  public Car(String vin, String make, String model, int price, int mileage, String color){
    this.vin=vin;
    this.make=make;
    this.model=model;
    this.price=price;
    this.mileage=mileage;
    this.color = color;

    //this.pind=0;
    //this.mind=0;

  }

	/**
	 * Getter for the VIN attribute
	 *
	 * @return 	String The VIN
	 */
	public String getVIN(){
    return vin;
  }

	/**
	 * Getter for the make attribute
	 *
	 * @return 	String The make
	 */
	public String getMake(){
    return make;
  }

	/**
	 * Getter for the model attribute
	 *
	 * @return 	String The model
	 */
	public String getModel(){
    return model;
  }

	/**
	 * Getter for the price attribute
	 *
	 * @return 	String The price
	 */
	public int getPrice(){
    return price;
  }

	/**
	 * Getter for the mileage attribute
	 *
	 * @return 	String The mileage
	 */
	public int getMileage(){
    return mileage;
  }

	/**
	 * Getter for the color attribute
	 *
	 * @return 	String The color
	 */
	public String getColor(){
    return color;
  }

	/**
	 * Setter for the price attribute
	 *
	 * @param 	newPrice The new Price
	 */
	public void setPrice(int newPrice){
    price = newPrice;
  }

	/**
	 * Setter for the mileage attribute
	 *
	 * @param 	newMileage The new Mileage
	 */
	public void setMileage(int newMileage){
    mileage=newMileage;
  }


	/**
	 * Setter for the color attribute
	 *
	 * @param 	newColor The new color
	 */
	public void setColor(String newColor){
    color=newColor;
  }
  public void setPind(int pind){
    this.pind = pind;
  }
  public void setMind(int mind){
    this.mind=mind;
  }
  public int getPind(){
    return pind;
  }
  public int getMind(){
    return mind;
  }

}
