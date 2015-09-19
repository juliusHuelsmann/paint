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


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import model.settings.State;


/**
 * Kind of a serializable BufferedImage.
 * @author Julius Huelsmann
 * @version %I%, %U%
 */
public class SerializBufferedImage implements Serializable {

	
	/**
	 * Generated Serial version.
	 */
	private static final long serialVersionUID = -613619478144485339L;


	/**
	 * The content of the Serializable BufferedImage.
	 */
	private BufferedImage bi_content;
	
	
	/**
	 * is only filled if needed (thus if the file is going to be saved).
	 */
	private int[] rgb_content;
	
	/**
	 * Width and height for transforming the one dimensional content array into
	 * a two-dimensional one. Both values are saved for maintaining correctness.
	 */
	private int width, height;
	
	
	/**
	 * Constructor: saves its content.
	 * @param _bi the BufferedImage to be saved.
	 */
	public SerializBufferedImage(final BufferedImage _bi) {
		this.bi_content = _bi;
	}
	

	
	/**
	 * Prepares for saving the file; thus is deleting the BufferedImage after
	 * its content is safely stored inside the byte array.
	 */
	public final void pack() {
		
		if (bi_content != null) {
			
			//catch RGB - Array from BufferedImage
			rgb_content = new int[bi_content.getWidth() 
			                      * bi_content.getHeight()];
			width = bi_content.getWidth();
			height = bi_content.getHeight();

			rgb_content = bi_content.getRGB(0, 0, bi_content.getWidth(),
					bi_content.getHeight(), rgb_content, 
					0, bi_content.getWidth());
			
			//remove the BufferdImage because it is not possible to save it.
			bi_content = null;
		} else {
			State.getLogger().severe("Prepare for save: Content to "
					+ "be saved is null");
		}
	}
	
	
	/**
	 * Restore the BufferedImage from the field rgb_content with the 
	 * help of the saved width and height.
	 * 
	 * Afterwards delete the content array and the content of the fields width
	 * and height.
	 * 
	 */
	public final void restore() {
		
		if (rgb_content != null) {
			
			if (bi_content == null) {
				
				if (width * height == rgb_content.length) {

					bi_content = new BufferedImage(
							width, height, BufferedImage.TYPE_INT_ARGB);
					bi_content.setRGB(0, 0, width, height,
							rgb_content, 0, bi_content.getWidth());
					
					//set the save - values to null.
					width = 0;
					height = 0;
					rgb_content = null;
				} else {

					State.getLogger().severe("Implementation error."
							+ " Size does not match.");
				}
			} else {

				State.getLogger().severe("Do not have to restore:"
						+ "Content has already been restored.");
			}
		} else {
			State.getLogger().severe("Unable to restore: Content is null");
		}
	}
	
	
	/**
	 * @param _bi the BufferedImage to set.
	 */
	public final void setContent(final BufferedImage _bi) {
		bi_content = _bi;
	}

	/**
	 * @return the BufferedImage.
	 */
	public final BufferedImage getContent() {
		if (bi_content == null && rgb_content != null) {
			restore();
		}
		return bi_content;
	}



	/**
	 * @return the height.
	 */
	public final int getHeight() {
		if (getContent() == null) {
			return 0;
		}
		
		return getContent().getHeight();
	}
	
	/**
	 * @return the width
	 */
	public final int getWidth() {
		
		if (getContent() == null) {
			return 0;
		}
		
		return getContent().getWidth();
	}

	
	
	public final int getGrayifiedValue(final int _x, final int _y) {
		Color c = new Color(bi_content.getRGB(_x, _y));
		return (c.getRed() + c.getGreen() + c.getBlue()) / 3;
	}


	/**
	 * Returns an array of integer pixels in the default RGB color model
	 * (TYPE_INT_ARGB) and default sRGB color space, from a portion of the 
	 * image data. Color conversion takes place if the default model does not
	 * match the image ColorModel. There are only 8-bits of precision for each 
	 * color component in the returned data when using this method. With a 
	 * specified coordinate (x, y) in the image, the ARGB pixel can be accessed 
	 * in this way:
	 * 
     * pixel   = rgbArray[offset + (y-startY)*scanSize + (x-startX)]; 
	 * An ArrayOutOfBoundsException may be thrown if the region is not 
	 * in bounds. However, explicit bounds checking is not guaranteed.
	 * 
	 * @param _startX 		the starting X coordinate
	 * @param _startY		the starting Y coordinate
	 * @param _w 			width of region
	 * @param _h 			height of region
	 * @param _rgbArray 		if not null, the RGB pixels are written here
	 * @param _offset 		offset into the rgbArray
	 * @param _scansize 		scanline stride for the rgbArray
	 * @return 			array of RGB pixels.
	 */
	public final int[] getRGB(
			final int _startX, final int _startY, final int _w, 
			final int _h, final int[] _rgbArray, 
			final int _offset, final int _scansize) {
		
		if (bi_content == null) {
			return null;
		}
		
		return bi_content.getRGB(
				_startX, _startY, _w, _h, _rgbArray, _offset, _scansize);
	}
	
}
