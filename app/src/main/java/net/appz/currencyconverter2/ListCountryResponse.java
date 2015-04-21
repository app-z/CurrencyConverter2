package net.appz.currencyconverter2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListCountryResponse {

	@SerializedName("results")
	@Expose 
	Map<String, Country> countries = new HashMap<String, Country>();

}
