package com.atlassian.selenium;

import com.atlassian.selenium.keyboard.KeyEvent;
import com.atlassian.selenium.keyboard.KeyEventSequence;
import com.atlassian.webtest.ui.keys.KeyEventType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * This test will generate to stdout the json for the character to keyevent mapping.
 *
 */
public class TestGenerateJSonKeyEventsForCharacters extends TestCase
{

    Set<Browser> allBrowsers = new HashSet<Browser>();
    Set<Browser> firefoxOpera = new HashSet<Browser>();
    Set<Browser> chromeIESafari = new HashSet<Browser>();
    Set<Browser> fireFoxOnly = new HashSet<Browser>();

    final KeyEvent shiftKeyDown = new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_SHIFT,allBrowsers,true,false,false,false,true,false);
    final KeyEvent shiftKeyUp = new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_SHIFT,allBrowsers,false,false,false,false,true,false);

    public void testCreateObjectsAndJSONify() throws Exception {
        Map<String, KeyEventSequence> cksl = new TreeMap<String, KeyEventSequence>();
        allBrowsers.add(Browser.CHROME);
        allBrowsers.add(Browser.FIREFOX);
        allBrowsers.add(Browser.IE);
        allBrowsers.add(Browser.OPERA);
        allBrowsers.add(Browser.SAFARI);
        allBrowsers.add(Browser.UNKNOWN);
        firefoxOpera.add(Browser.FIREFOX);
        firefoxOpera.add(Browser.OPERA);
        chromeIESafari.add(Browser.CHROME);
        chromeIESafari.add(Browser.IE);
        chromeIESafari.add(Browser.SAFARI);
        fireFoxOnly.add(Browser.FIREFOX);
        for (Character c = 'a'; c <= 'z'; c++ )
        {
            List<KeyEvent> ke = new ArrayList<KeyEvent>();
            ke.add(new KeyEvent(KeyEventType.KEYDOWN,c.toString().toUpperCase().charAt(0),allBrowsers,false,false,false,false,true,false));
            ke.add(new KeyEvent(KeyEventType.KEYPRESS,c,allBrowsers,false,false,false,false,false,true));
            ke.add(new KeyEvent(KeyEventType.KEYUP,c.toString().toUpperCase().charAt(0),allBrowsers,false,false,false,false,true,false));
            KeyEventSequence cks = new KeyEventSequence(c.toString(),ke);
            cksl.put(cks.getIdentifier(),cks);
        }
        for (Character c = 'A'; c <= 'Z'; c++ )
        {
            List<KeyEvent> ke = new ArrayList<KeyEvent>();
            ke.add(shiftKeyDown);
            ke.add(new KeyEvent(KeyEventType.KEYDOWN,c.toString().toUpperCase().charAt(0),allBrowsers,true,false,false,false,true,false));
            ke.add(new KeyEvent(KeyEventType.KEYPRESS,c,allBrowsers,true,false,false,false,false,true));
            ke.add(new KeyEvent(KeyEventType.KEYUP,c.toString().toUpperCase().charAt(0),allBrowsers,true,false,false,false,true,false));
            ke.add(shiftKeyUp);
            KeyEventSequence cks = new KeyEventSequence(c.toString(),ke);
            cksl.put(cks.getIdentifier(),cks);
        }
        for (Character c = '0'; c <= '9'; c++ )
        {
            List<KeyEvent> ke = new ArrayList<KeyEvent>();
            ke.add(new KeyEvent(KeyEventType.KEYDOWN,c,allBrowsers,false,false,false,false,true,false));
            ke.add(new KeyEvent(KeyEventType.KEYPRESS,c,allBrowsers,false,false,false,false,false,true));
            ke.add(new KeyEvent(KeyEventType.KEYUP,c,allBrowsers,false,false,false,false,true,false));
            KeyEventSequence cks = new KeyEventSequence(c.toString(),ke);
            cksl.put(cks.getIdentifier(),cks);
        }
        //Now do the shifted versions of above
        List<KeyEvent> ke = new ArrayList<KeyEvent>();
        ke.add(shiftKeyDown);
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,'1',allBrowsers,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'!',allBrowsers,true,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,'1',allBrowsers,true,false,false,false,true,false));
        ke.add(shiftKeyUp);
        KeyEventSequence cks = new KeyEventSequence("!",ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(shiftKeyDown);
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,'2',allBrowsers,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'@',allBrowsers,true,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,'2',allBrowsers,true,false,false,false,true,false));
        ke.add(shiftKeyUp);
        cks = new KeyEventSequence("@",ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(shiftKeyDown);
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,'3',allBrowsers,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'#',allBrowsers,true,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,'3',allBrowsers,true,false,false,false,true,false));
        ke.add(shiftKeyUp);
        cks = new KeyEventSequence("#",ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(shiftKeyDown);
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,'4',allBrowsers,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'$',allBrowsers,true,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,'4',allBrowsers,true,false,false,false,true,false));
        ke.add(shiftKeyUp);
        cks = new KeyEventSequence("$",ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(shiftKeyDown);
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,'5',allBrowsers,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'%',allBrowsers,true,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,'5',allBrowsers,true,false,false,false,true,false));
        ke.add(shiftKeyUp);
        cks = new KeyEventSequence("%",ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(shiftKeyDown);
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,'6',allBrowsers,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'^',allBrowsers,true,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,'6',allBrowsers,true,false,false,false,true,false));
        ke.add(shiftKeyUp);
        cks = new KeyEventSequence("^",ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(shiftKeyDown);
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,'7',allBrowsers,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'&',allBrowsers,true,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,'7',allBrowsers,true,false,false,false,true,false));
        ke.add(shiftKeyUp);
        cks = new KeyEventSequence("&",ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(shiftKeyDown);
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,'8',allBrowsers,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'*',allBrowsers,true,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,'8',allBrowsers,true,false,false,false,true,false));
        ke.add(shiftKeyUp);
        cks = new KeyEventSequence("*",ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(shiftKeyDown);
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,'9',allBrowsers,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'(',allBrowsers,true,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,'9',allBrowsers,true,false,false,false,true,false));
        ke.add(shiftKeyUp);
        cks = new KeyEventSequence("(",ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(shiftKeyDown);
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,'0',allBrowsers,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,')',allBrowsers,true,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,'0',allBrowsers,true,false,false,false,true,false));
        ke.add(shiftKeyUp);
        cks = new KeyEventSequence(")",ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,' ',allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,' ',allBrowsers,false,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,' ',allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_SPACE),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_ENTER,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_ENTER,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_ENTER,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_ENTER),ke);
        cksl.put(cks.getIdentifier(),cks);

        // Javascripts REAL return character
        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,13,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,13,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,13,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",13),ke);
        cksl.put(cks.getIdentifier(),cks);


        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_TAB,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_TAB,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_TAB),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_ESCAPE,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_ESCAPE,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_ESCAPE,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_ESCAPE),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_BACK_SPACE,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_BACK_SPACE,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_BACK_SPACE,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_BACK_SPACE),ke);
        cksl.put(cks.getIdentifier(),cks);

        createSymbols(cksl);
        createSpecialKeys(cksl);
        createFNKeys(cksl);
        createModifierKeys(cksl);


        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        // Uncomment to spit the json out.
        // Copy to charactersKeySequences.json
        // Need to fix 2 errrors gson makes """ isn't escaped and either is "\"

//        System.out.println(gson.toJson(cksl));

        // Test if the json can be read back in
        String characterArrayJSON = readFile("charactersKeySequences.json");
        Type collectionType = new TypeToken<HashMap<String, KeyEventSequence>>(){}.getType();
        Map cksMap = gson.fromJson(characterArrayJSON,collectionType);
//        System.out.println(cksMap.toString());


    }

    private void createModifierKeys(Map<String, KeyEventSequence> cksl)
    {
        List<KeyEvent> ke = new ArrayList<KeyEvent>();
        ke.add(shiftKeyDown);
        ke.add(shiftKeyUp);
        KeyEventSequence cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_SHIFT),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_ALT,allBrowsers,false,true,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_ALT,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_ALT),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_CONTROL,allBrowsers,false,false,true,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_CONTROL,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_CONTROL),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_META,allBrowsers,false,false,false,true,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_META,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_META),ke);
        cksl.put(cks.getIdentifier(),cks);

    }

    private void createFNKeys(Map<String, KeyEventSequence> cksl)
    {
        Set<Browser> chromeOnly = new HashSet<Browser>();
        chromeOnly.add(Browser.CHROME);

        List<KeyEvent> ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_F1,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_F1,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,63236,chromeOnly,false,false,false,false,true,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_F1,allBrowsers,false,false,false,false,true,false));
        KeyEventSequence cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_F1),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_F2,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_F2,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,63237,chromeOnly,false,false,false,false,true,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_F2,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_F2),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_F3,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_F3,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,63238,chromeOnly,false,false,false,false,true,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_F3,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_F3),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_F4,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_F4,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,63239,chromeOnly,false,false,false,false,true,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_F4,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_F4),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_F5,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_F5,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,63240,chromeOnly,false,false,false,false,true,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_F5,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_F5),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_F6,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_F6,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,63241,chromeOnly,false,false,false,false,true,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_F6,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_F6),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_F7,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_F7,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,63242,chromeOnly,false,false,false,false,true,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_F7,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_F7),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_F8,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_F8,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,63243,chromeOnly,false,false,false,false,true,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_F8,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_F8),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_F9,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_F9,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,63244,chromeOnly,false,false,false,false,true,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_F9,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_F9),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_F10,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_F10,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,63245,chromeOnly,false,false,false,false,true,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_F10,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_F10),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_F11,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_F11,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,63246,chromeOnly,false,false,false,false,true,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_F11,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_F11),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_F12,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_F12,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,63247,chromeOnly,false,false,false,false,true,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_F12,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_F12),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_F13,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_F13,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,63248,chromeOnly,false,false,false,false,true,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_F13,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_F13),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_F14,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_F14,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,63249,chromeOnly,false,false,false,false,true,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_F14,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_F14),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_F15,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_F15,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,63250,chromeOnly,false,false,false,false,true,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_F15,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_F15),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_F16,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_F16,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,63251,chromeOnly,false,false,false,false,true,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_F16,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_F16),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_F17,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_F17,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,63252,chromeOnly,false,false,false,false,true,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_F17,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_F17),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_F18,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_F18,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,63253,chromeOnly,false,false,false,false,true,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_F18,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_F18),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_F19,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_F19,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,63254,chromeOnly,false,false,false,false,true,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_F19,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_F19),ke);
        cksl.put(cks.getIdentifier(),cks);

    }

    private void createSpecialKeys(Map<String, KeyEventSequence> cksl)
    {
        List<KeyEvent> ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_LEFT,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_LEFT,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_LEFT,allBrowsers,false,false,false,false,true,false));
        KeyEventSequence cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_LEFT),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_RIGHT,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_RIGHT,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_RIGHT,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_RIGHT),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_UP,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_UP,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_UP,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_UP),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_DOWN,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_DOWN,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_DOWN,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_DOWN),ke);
        cksl.put(cks.getIdentifier(),cks);


        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_INSERT,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_INSERT,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_INSERT,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_INSERT),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_DELETE,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_DELETE,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_DELETE,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_DELETE),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_HOME,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_HOME,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_HOME,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_HOME),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_END,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_END,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_END,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_END),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_PAGE_UP,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_PAGE_UP,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_PAGE_UP,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_PAGE_UP),ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,java.awt.event.KeyEvent.VK_PAGE_DOWN,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,java.awt.event.KeyEvent.VK_PAGE_DOWN,fireFoxOnly,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,java.awt.event.KeyEvent.VK_PAGE_DOWN,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence(String.format("0x%x",java.awt.event.KeyEvent.VK_PAGE_DOWN),ke);
        cksl.put(cks.getIdentifier(),cks);

    }

    private void createSymbols(Map<String, KeyEventSequence> cksl)
    {
        List<KeyEvent> ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,';',firefoxOpera,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,186,chromeIESafari,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,';',allBrowsers,false,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,';',firefoxOpera,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,186,chromeIESafari,false,false,false,false,true,false));
        KeyEventSequence cks = new KeyEventSequence(";",ke);
        cksl.put(cks.getIdentifier(),cks);


        ke = new ArrayList<KeyEvent>();
        ke.add(shiftKeyDown);
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,';',firefoxOpera,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,186,chromeIESafari,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,':',allBrowsers,true,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,';',firefoxOpera,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,186,chromeIESafari,true,false,false,false,true,false));
        ke.add(shiftKeyUp);
        cks = new KeyEventSequence(":",ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,'=',firefoxOpera,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,187,chromeIESafari,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'=',allBrowsers,false,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,'=',firefoxOpera,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,187,chromeIESafari,false,false,false,false,true,false));
        cks = new KeyEventSequence("=",ke);
        cksl.put(cks.getIdentifier(),cks);


        ke = new ArrayList<KeyEvent>();
        ke.add(shiftKeyDown);
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,'=',firefoxOpera,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,187,chromeIESafari,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'+',allBrowsers,true,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,'=',firefoxOpera,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,187,chromeIESafari,true,false,false,false,true,false));
        ke.add(shiftKeyUp);
        cks = new KeyEventSequence("+",ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,188,firefoxOpera,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,188,chromeIESafari,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,',',allBrowsers,false,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,188,firefoxOpera,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,188,chromeIESafari,false,false,false,false,true,false));
        cks = new KeyEventSequence(",",ke);
        cksl.put(cks.getIdentifier(),cks);


        ke = new ArrayList<KeyEvent>();
        ke.add(shiftKeyDown);
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,188,firefoxOpera,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,188,chromeIESafari,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'<',allBrowsers,true,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,188,firefoxOpera,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,188,chromeIESafari,true,false,false,false,true,false));
        ke.add(shiftKeyUp);
        cks = new KeyEventSequence("<",ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,109,firefoxOpera,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,189,chromeIESafari,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'-',allBrowsers,false,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,109,firefoxOpera,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,189,chromeIESafari,false,false,false,false,true,false));
        cks = new KeyEventSequence("-",ke);
        cksl.put(cks.getIdentifier(),cks);


        ke = new ArrayList<KeyEvent>();
        ke.add(shiftKeyDown);
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,109,firefoxOpera,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,189,chromeIESafari,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'_',allBrowsers,true,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,109,firefoxOpera,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,189,chromeIESafari,true,false,false,false,true,false));
        ke.add(shiftKeyUp);
        cks = new KeyEventSequence("_",ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,190,firefoxOpera,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,190,chromeIESafari,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'.',allBrowsers,false,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,190,firefoxOpera,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,190,chromeIESafari,false,false,false,false,true,false));
        cks = new KeyEventSequence(".",ke);
        cksl.put(cks.getIdentifier(),cks);


        ke = new ArrayList<KeyEvent>();
        ke.add(shiftKeyDown);
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,190,firefoxOpera,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,190,chromeIESafari,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'>',allBrowsers,true,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,190,firefoxOpera,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,190,chromeIESafari,true,false,false,false,true,false));
        ke.add(shiftKeyUp);
        cks = new KeyEventSequence(">",ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,191,firefoxOpera,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,191,chromeIESafari,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'/',allBrowsers,false,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,191,firefoxOpera,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,191,chromeIESafari,false,false,false,false,true,false));
        cks = new KeyEventSequence("/",ke);
        cksl.put(cks.getIdentifier(),cks);


        ke = new ArrayList<KeyEvent>();
        ke.add(shiftKeyDown);
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,191,firefoxOpera,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,191,chromeIESafari,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'?',allBrowsers,true,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,191,firefoxOpera,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,191,chromeIESafari,true,false,false,false,true,false));
        ke.add(shiftKeyUp);
        cks = new KeyEventSequence("?",ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,192,firefoxOpera,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,192,chromeIESafari,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'`',allBrowsers,false,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,192,firefoxOpera,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,192,chromeIESafari,false,false,false,false,true,false));
        cks = new KeyEventSequence("`",ke);
        cksl.put(cks.getIdentifier(),cks);


        ke = new ArrayList<KeyEvent>();
        ke.add(shiftKeyDown);
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,192,firefoxOpera,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,192,chromeIESafari,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'~',allBrowsers,true,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,192,firefoxOpera,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYUP,192,chromeIESafari,true,false,false,false,true,false));
        ke.add(shiftKeyUp);
        cks = new KeyEventSequence("~",ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,219,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'[',allBrowsers,false,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,219,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence("[",ke);
        cksl.put(cks.getIdentifier(),cks);


        ke = new ArrayList<KeyEvent>();
        ke.add(shiftKeyDown);
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,219,allBrowsers,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'{',allBrowsers,true,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,219,allBrowsers,true,false,false,false,true,false));
        ke.add(shiftKeyUp);
        cks = new KeyEventSequence("{",ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,220,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'\\',allBrowsers,false,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,220,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence("\\",ke);
        cksl.put(cks.getIdentifier(),cks);


        ke = new ArrayList<KeyEvent>();
        ke.add(shiftKeyDown);
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,220,allBrowsers,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'|',allBrowsers,true,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,220,allBrowsers,true,false,false,false,true,false));
        ke.add(shiftKeyUp);
        cks = new KeyEventSequence("|",ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,221,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,']',allBrowsers,false,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,221,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence("]",ke);
        cksl.put(cks.getIdentifier(),cks);


        ke = new ArrayList<KeyEvent>();
        ke.add(shiftKeyDown);
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,221,allBrowsers,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'}',allBrowsers,true,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,221,allBrowsers,true,false,false,false,true,false));
        ke.add(shiftKeyUp);
        cks = new KeyEventSequence("}",ke);
        cksl.put(cks.getIdentifier(),cks);

        ke = new ArrayList<KeyEvent>();
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,222,allBrowsers,false,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'\'',allBrowsers,false,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,219,allBrowsers,false,false,false,false,true,false));
        cks = new KeyEventSequence("'",ke);
        cksl.put(cks.getIdentifier(),cks);


        ke = new ArrayList<KeyEvent>();
        ke.add(shiftKeyDown);
        ke.add(new KeyEvent(KeyEventType.KEYDOWN,219,allBrowsers,true,false,false,false,true,false));
        ke.add(new KeyEvent(KeyEventType.KEYPRESS,'"',allBrowsers,true,false,false,false,false,true));
        ke.add(new KeyEvent(KeyEventType.KEYUP,219,allBrowsers,true,false,false,false,true,false));
        ke.add(shiftKeyUp);
        cks = new KeyEventSequence("\"",ke);
        cksl.put(cks.getIdentifier(),cks);

    }

    private static String readFile(String file) throws IOException
    {
        BufferedReader reader =  new BufferedReader(new InputStreamReader(ClassLoader.getSystemClassLoader().getResourceAsStream(file)));

        String line = reader.readLine();
        StringBuffer contents = new StringBuffer();

        while(line != null)
        {
            contents.append(line).append("\n");
            line = reader.readLine();
        }

        return contents.toString();
    }

}