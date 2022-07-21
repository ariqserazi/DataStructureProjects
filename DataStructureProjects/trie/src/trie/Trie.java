package trie;

import java.util.ArrayList;

import javax.security.auth.Subject;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */

public class Trie {
  
	// prevent instantiation
	private Trie() { }
   
/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords){
		TrieNode root = new TrieNode(null, null, null);
		if(allWords.length==0){
			return root;
		}
		Indexes Index = new Indexes(0, (short)0 , (short) (allWords[0].length()-1));
		TrieNode item1 = new TrieNode(Index, null, null);
		root.firstChild = item1;
	 	TrieNode Child = root.firstChild;
		TrieNode temp = null;
		for(int i = 1; i < allWords.length; i++){
			String preFix = "";
			String input = allWords[i];
			while(Child != null){
			int start = Child.substr.startIndex;
			int word = Child.substr.wordIndex;
			int end = Child.substr.endIndex;
			String TheWord = allWords[word].substring(start, end+1);
				if(TheWord.charAt(0) != input.charAt(start)){ 
					temp = Child;
					Child = Child.sibling;
					continue;
				}
				if(input.indexOf(preFix+TheWord) == 0){ 
					preFix = preFix + TheWord;
					temp = Child;
					Child = Child.firstChild;
					continue;
				}   
				int Occ = 0;
				for(int j = 0; j < TheWord.length(); j++){
					if(input.charAt(start+j) == TheWord.charAt(j)) {
						Occ++;
					}else {
						break;
					}
				}
				int TruePrefix = start + Occ;
				Indexes Index2 = new Indexes(word, (short)TruePrefix, (short)end);
				TrieNode temp2 = new TrieNode(Index2, Child.firstChild, null);
				Child.substr.endIndex = (short) (TruePrefix-1);
				Child.firstChild = temp2;
				preFix = preFix + allWords[word].substring(start, TruePrefix);
				temp = Child;
				Child = Child.firstChild;
				}
			TrieNode next = new TrieNode(new Indexes(i, (short)(preFix.length()), (short)(input.length()-1)), null, null);
			temp.sibling = next; 
			Child = root.firstChild; 
			temp = null;
			}
			return root;
		}
		/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,String[] allWords, String prefix){
		ArrayList<TrieNode> List = new ArrayList<>();
		TrieNode Child = root.firstChild;
		String preFix ="";
		TrieNode temp = null;
		while(prefix.length() > preFix.length() && Child != null){
			int wordIndex = Child.substr.wordIndex;
			int startIndex = Child.substr.startIndex;
			int endIndex = Child.substr.endIndex;
			temp = Child;
			String currentWord = allWords[wordIndex].substring(startIndex, endIndex+1);
			if((preFix+currentWord).indexOf(prefix) == 0){
				Child = Child.firstChild;
				break;
			}
			if(prefix.indexOf(preFix+currentWord) == 0){
				preFix = preFix + currentWord;
				Child = Child.firstChild;
				continue;
			}
			Child = Child.sibling;
		}
		int Index3 = temp.substr.wordIndex;
		if(Child == null){ 
			if(allWords[Index3].indexOf(prefix) == 0){
				List.add(temp);
				return List;
			}
		}
		inputTheWords(List, Child);
		return List;
	}
	public static void inputTheWords(ArrayList<TrieNode> completion, TrieNode firstword){
		if(firstword==null){
			return;
		}
		if(firstword.firstChild == null){
			completion.add(firstword);
			inputTheWords(completion, firstword.sibling);
			return;
			
		}
		inputTheWords(completion, firstword.firstChild);
		inputTheWords(completion, firstword.sibling);
	}
	public static void print(TrieNode root, String[] allWords){
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex].substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }