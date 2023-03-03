package utils.design.decorator.impl;


import utils.design.decorator.Sharp;

/**
 * @author lwq
 * @date 2020/5/9 0009
 */
public class Rectangle implements Sharp {
	@Override
	public void draw() {
		System.out.println("Sharp：Rectangle");
	}
}
