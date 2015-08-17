package model.util;



/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


public class BinKoeff {
  
	private double [][] d;
	
	
	
	public BinKoeff() {
		final int size = 900;
		d = new double[size][size];
		
		
		// 1
		// 1 1
		// 1 2 1
		// 1 3 3 1
		// 1 4 6 4 1
		// 1 5 10 10 5 1
		
		d[0][0] = 1;
		
		
		for (int i = 1; i < d.length; i++) {
			for (int j = 0; j <= i; j++) {
				
				double sum1 = 0, sum2 = 0;
				if (i - 1 >= 0 && j - 1 >= 0) {

					sum1 = d[i - 1][j - 1];
				}
				if (i - 1 >= 0 && j >= 0) {

					sum2 = d[i - 1][j];
				}
				
				d[i][j] = sum1 + sum2;
			}
		}
		
		double max = Double.MIN_VALUE;
		double min = Double.MAX_VALUE;
		

		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j <= i; j++) {

				if (d[i][j] > max) {
					max = d[i][j];
				}
				if (d[i][j] < min) {
					min = d[i][j];
				}
			//	System.out.print(d[i][j] + "\t");
			}
		//	System.out.println();
		}

		System.out.println("max " + max);
		System.out.println("min " + min);
		
	}
	private static BinKoeff instance;
	
	public static BinKoeff getInstance() {
		
		if (instance == null) {
			instance = new BinKoeff();
		}
		return instance;
	}
	
	
  
  /** Berechnet den Binomialkoeffizienten explizit */
  public static double binKoeff(int m,int k) {
	  return getInstance().d[m][k];
  }

  /** Testet funktionalitaet und performance der beiden Methoden */ 
  public static void main (String[] args) {
	  new BinKoeff();
  }
}
