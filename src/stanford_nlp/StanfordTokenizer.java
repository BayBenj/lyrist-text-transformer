package stanford_nlp;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.PTBTokenizer;

public class StanfordTokenizer {
	  public void run() throws IOException {
		  String[] args = {"/Users/Benjamin/Documents/workspace/pop-star/Pop*/src/sorrow.txt"};
		  for (String arg : args) {
			  // option #1: By sentence.
			  DocumentPreprocessor dp = new DocumentPreprocessor(arg);
			  System.out.println("\nPRINTING BY SENTENCE");
			  for (List<HasWord> sentence : dp) {
				  System.out.println(sentence);
			  }
			  // option #2: By token
			  PTBTokenizer<CoreLabel> ptbt = new PTBTokenizer<>(new FileReader(arg),
		          new CoreLabelTokenFactory(), "invertible=true,tokenizeNLs=true,americanize=true");
			  System.out.println("\nPRINTING BY TOKEN");
			  while (ptbt.hasNext()) {
				  CoreLabel label = ptbt.next();
				  System.out.println(label);
			  }
		  }
	  }
}
