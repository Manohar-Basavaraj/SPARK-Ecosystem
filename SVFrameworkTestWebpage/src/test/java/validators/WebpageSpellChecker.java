package validators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.engine.Word;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;
import com.swabunga.spell.event.TeXWordFinder;

public class WebpageSpellChecker implements SpellCheckListener {
	private SpellChecker spellChecker;
	private List<String> misspelledWords;

	/**
	 * get a list of misspelled words from the text
	 * 
	 * @param text
	 */
	public List<String> getMisspelledWords(String text) {
		StringWordTokenizer texTok = new StringWordTokenizer(text, new TeXWordFinder());
		spellChecker.checkSpelling(texTok);
		return misspelledWords;
	}

	private static SpellDictionaryHashMap dictionaryHashMap;

	static {
		File dict = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\dictionary\\Dictionary.txt");
		try {
			dictionaryHashMap = new SpellDictionaryHashMap(dict);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initialize() {
		spellChecker = new SpellChecker(dictionaryHashMap);
		spellChecker.addSpellCheckListener(this);
	}

	public WebpageSpellChecker() {
		misspelledWords = new ArrayList<String>();
		initialize();
	}

	public Map<String, String> showSuggestions(String line) {
		List<String> misSpelledWords = getMisspelledWords(line);
		Map<String, String> misspelledSuggestions = new HashMap<String, String>();
		
		for (String misSpelledWord : misSpelledWords) {
			List<String> suggestions = getSuggestions(misSpelledWord);
			if (suggestions.size() == 0)
				continue;
			// String bestSuggestion = suggestions.get(0);
			misspelledSuggestions.put(misSpelledWord, suggestions.toString());
			// line = line.replace(misSpelledWord, bestSuggestion);
		}
		
		return misspelledSuggestions;
	}

	public List<String> getSuggestions(String misspelledWord) {

		@SuppressWarnings("unchecked")
		List<Word> su99esti0ns = spellChecker.getSuggestions(misspelledWord, 0);
		List<String> suggestions = new ArrayList<String>();
		for (Word suggestion : su99esti0ns) {
			suggestions.add(suggestion.getWord());
		}

		return suggestions;
	}

	// @Override
	public void spellingError(SpellCheckEvent event) {
		event.ignoreWord(true);
		misspelledWords.add(event.getInvalidWord());
	}
}