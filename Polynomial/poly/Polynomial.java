package poly;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class implements evaluate, add and multiply for polynomials.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param sc Scanner from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 * @return The polynomial linked list (front node) constructed from coefficients and
	 *         degrees read from scanner
	 */
	public static Node read(Scanner sc) 
	throws IOException {
		Node poly = null;
		while (sc.hasNextLine()) {
			Scanner scLine = new Scanner(sc.nextLine());
			poly = new Node(scLine.nextFloat(), scLine.nextInt(), poly);
			scLine.close();
		}
		return poly;
	}

	/**
	 * Returns the sum of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list
	 * @return A new polynomial which is the sum of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node add(Node poly1, Node poly2) {
		Node last= null;
		Node front = null;
		Node current = null;
		if(poly1 == null && poly2 == null) {
			return front;
		}
		while(poly1 != null && poly2 != null) {
			if(poly1.term.degree < poly2.term.degree) {
				current = new Node(poly1.term.coeff, poly1.term.degree, null);
				if(last == null){
					front = current;

				}else{
					last.next = current;
				}
				last = current;
				poly1 = poly1.next;
			}else if(poly2.term.degree < poly1.term.degree) {
				
				current = new Node(poly2.term.coeff, poly2.term.degree, null);
				if(last == null){
					front = current;

				}else{
					last.next = current;
				}
				last = current;
				poly2 = poly2.next;
			}else if(poly1.term.degree == poly2.term.degree){
				current = new Node(poly1.term.coeff + poly2.term.coeff, poly1.term.degree,null);
				if(last == null){
					front = current;

				}else{
					last.next = current;
				}
				last = current;
				poly1 = poly1.next;
				poly2 = poly2.next;
			}
		}
		if(poly1 != null) {
			while(poly1 != null) {
				current = new Node(poly1.term.coeff, poly1.term.degree, null);
				if(last == null){
					front = current;

				}else{
					last.next = current;
				}
					last = current;
					poly1 = poly1.next;
				}
		}else if(poly2 != null){
			while(poly2 != null) {
				current = new Node(poly2.term.coeff, poly2.term.degree, null);
				if(last == null){
					front = current;

				}else{
					last.next = current;
				}
					last = current;
					poly2 = poly2.next;
				}
		}
		Node prev = null;
		current = front;
		while(current != null) {
			if(current.term.coeff == 0) {
				if(current == front) {
					front = current.next;
				}else{
					prev.next = current.next;
				}
			}else{
				prev = current;
			}
			current = current.next;
		}

		return front;
	}

	/**
	 * Returns the product of two polynomials - DOES NOT change either of the input polynomials.
	 * The returned polynomial MUST have all new nodes. In other words, none of the nodes
	 * of the input polynomials can be in the result.
	 * 
	 * @param poly1 First input polynomial (front of polynomial linked list)
	 * @param poly2 Second input polynomial (front of polynomial linked list)
	 * @return A new polynomial which is the product of the input polynomials - the returned node
	 *         is the front of the result polynomial
	 */
	public static Node multiply(Node poly1, Node poly2) {
		Node front = null;
		Node last = null;
		Node sum = null;
		if(poly1 == null || poly2 == null) {
			return front;
		}

		for(Node current2 = poly2; current2 != null; current2 = current2.next) {
			for(Node current1 = poly1; current1 != null; current1 = current1.next) {
				Node current = new Node(current1.term.coeff * current2.term.coeff, current1.term.degree + current2.term.degree, null);
			if(last == null){
				front = current;
			}else{
				last.next = current;
			}
			last = current;
			}
			sum = add(sum, front);
			front = null;
			last = null;
		}
		return sum;
	}

	/**
	 * Evaluates a polynomial at a given value.
	 * 
	 * @param poly Polynomial (front of linked list) to be evaluated
	 * @param x Value at which evaluation is to be done
	 * @return Value of polynomial p at x
	 */
	public static float evaluate(Node poly, float x) {
		/** COMPLETE THIS METHOD **/
		// FOLLOWING LINE IS A PLACEHOLDER TO MAKE THIS METHOD COMPILE
		// CHANGE IT AS NEEDED FOR YOUR IMPLEMENTATION
		Node ptr = poly;
		float value = 0 ;
		while(ptr != null){
			int degree  = ptr.term.degree;
			float coeff = ptr.term.coeff;
			value += coeff * (Math.pow(x, degree));
			ptr = ptr.next;
		}

		return value;
	}

	/**
	 * Returns string representation of a polynomial
	 * 
	 * @param poly Polynomial (front of linked list)
	 * @return String representation, in descending order of degrees
	 */
	public static String toString(Node poly) {
		if (poly == null) {
			return "0";
		} 

		String retval = poly.term.toString();
		for (Node current = poly.next ; current != null ;
		current = current.next) {
			retval = current.term.toString() + " + " + retval;
		}
		return retval;
	}	
}