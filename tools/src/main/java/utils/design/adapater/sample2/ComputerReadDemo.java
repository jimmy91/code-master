package utils.design.adapater.sample2;

import utils.design.adapater.sample2.targetinterface.XiaomiComputer;
import utils.design.adapater.sample2.adaptee.TFCard;
import utils.design.adapater.sample2.adaptee.TFCardImpl;
import utils.design.adapater.sample2.adapter.SDAdapterTF;
import utils.design.adapater.sample2.targetinterface.Computer;
import utils.design.adapater.sample2.targetinterface.SDCard;
import utils.design.adapater.sample2.targetinterface.SDCardImpl;

/**
 * @author lwq
 * @date 2022/10/25 0025
 * @since
 */
public class ComputerReadDemo {

	public static void main(String[] args) {
		String s1 = beforeAdapte();
		System.out.println(s1);

		System.out.println("------------------------------");

		String s2 = afterAdapte();
		System.out.println(s2);
	}

	public static String beforeAdapte() {
		Computer computer = new XiaomiComputer();
		SDCard sdCard = new SDCardImpl();
		return computer.readSD(sdCard);
	}

	public static String afterAdapte() {
		Computer computer = new XiaomiComputer();
		TFCard tfCard = new TFCardImpl();
		SDCard sdCard = new SDAdapterTF(tfCard);
		return computer.readSD(sdCard);
	}
}
