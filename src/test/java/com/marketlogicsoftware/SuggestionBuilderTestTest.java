package com.marketlogicsoftware;

import static org.junit.Assert.*;

import com.marketlogicsoftware.SuggestionBuilderTest.Suggestion;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Test;

/**
 * Created by yoldeta on 2017-04-28.
 */

public class SuggestionBuilderTestTest {

  @Test
  public void aTest() {
    Set<String> stopWord = new HashSet<>(Arrays.asList("is", "can", "the"));
    List<String> tokens = Arrays.asList("The", "beautiful", "girl", "from", "the", "farmers", "market", ".", "I",
        "like", "chewing", "gum", ".");
    Set<Suggestion> suggestions = SuggestionBuilderTest.buildSuggestionsFromTokenStream(tokens.iterator(), stopWord);

    int tmp = 1 + 1;


  }
}
