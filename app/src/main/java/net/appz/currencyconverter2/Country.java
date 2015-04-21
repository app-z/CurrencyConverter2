package net.appz.currencyconverter2;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Country implements Comparable<Country>{

    @SerializedName(value="currencyId")
    @Expose
	String currencyId;
    @SerializedName(value="currencyName")
    @Expose
	String currencyName;
    @SerializedName(value="name")
    @Expose
	String name;
    @SerializedName(value="alpha3")
    @Expose
	String alpha3;

	@Override
	public int compareTo(Country another) {
		return currencyId.compareTo(another.currencyId);
	}
	
	@Override
	public
	String toString(){
		return this.currencyId;
	}

}
