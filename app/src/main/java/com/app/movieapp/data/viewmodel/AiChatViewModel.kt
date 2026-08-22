package com.app.movieapp.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

class AiChatViewModel : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                text = "Hey! I'm your AI Movie CineBot 🎬 (Feature currently in development with simulated demo responses).\n\nTell me your mood, favorite actors, or themes, and I'll share curated movie recommendations!",
                isUser = false
            )
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val dummyResponses = listOf(
        "[Demo Mode] If you loved Interstellar (2014), you must check out Arrival (2016) and Contact (1997) for incredible sci-fi depth and emotional stakes!",
        "[Demo Mode] Looking for high-octane action? Watch John Wick: Chapter 4 (2023), Mad Max: Fury Road (2015), or The Raid (2011) for non-stop adrenaline!",
        "[Demo Mode] For a cozy, feel-good evening, try The Grand Budapest Hotel (2014), Paddington 2 (2017), or Amélie (2001).",
        "[Demo Mode] Mind-bending thrillers worth your time: Shutter Island (2010), Memento (2000), Prisoners (2013), and Coherence (2013).",
        "[Demo Mode] Classic 90s cinema gems: The Shawshank Redemption (1994), Pulp Fiction (1994), Fight Club (1999), and Goodfellas (1990).",
        "[Demo Mode] Top-tier animated masterpieces: Spider-Man: Into the Spider-Verse (2018), Spirited Away (2001), and WALL-E (2008).",
        "[Demo Mode] Craving mystery and detective work? Go for Knives Out (2019), Zodiac (2007), Se7en (1995), or Gone Girl (2014).",
        "[Demo Mode] Epic space adventures you'll love: Dune: Part Two (2024), The Martian (2015), and Guardians of the Galaxy (2014).",
        "[Demo Mode] Unforgettable romantic dramas: La La Land (2016), Before Sunrise (1995), and About Time (2013).",
        "[Demo Mode] If you want edge-of-your-seat survival films, stream 127 Hours (2010), Gravity (2013), or The Revenant (2015).",
        "[Demo Mode] Masterclass horror films: Hereditary (2018), The Conjuring (2013), Get Out (2017), and A Quiet Place (2018).",
        "[Demo Mode] Gripping courtroom and legal dramas: 12 Angry Men (1957), A Few Good Men (1992), and Primal Fear (1996).",
        "[Demo Mode] Dark neo-noir recommendations: Blade Runner 2049 (2017), Drive (2011), and Nightcrawler (2014).",
        "[Demo Mode] Superb heist flicks to binge: Ocean's Eleven (2001), Baby Driver (2017), The Town (2010), and Heat (1995).",
        "[Demo Mode] Laugh-out-loud comedies: Superbad (2007), The Hangover (2009), and 21 Jump Street (2012).",
        "[Demo Mode] Inspiring biopics based on true stories: Oppenheimer (2023), The Social Network (2010), and The Wolf of Wall Street (2013).",
        "[Demo Mode] Psychological horror with slow burn: The Witch (2015), Midsommar (2019), and The Lighthouse (2019).",
        "[Demo Mode] Top martial arts movies: Ip Man (2008), Crouching Tiger, Hidden Dragon (2000), and Hero (2002).",
        "[Demo Mode] Dystopian future picks: Children of Men (2006), V for Vendetta (2005), and Snowpiercer (2013).",
        "[Demo Mode] Wholesome animated films for family night: Coco (2017), Ratatouille (2007), and Klaus (2019).",
        "[Demo Mode] Fast-paced crime thrillers: Sicario (2015), No Country for Old Men (2007), and The Departed (2006).",
        "[Demo Mode] Time loop and paradox movies: Edge of Tomorrow (2014), Source Code (2011), and Looper (2012).",
        "[Demo Mode] War epics with unmatched cinematography: 1917 (2019), Saving Private Ryan (1998), and Dunkirk (2017).",
        "[Demo Mode] Underappreciated indie gems: Ex Machina (2014), Whiplash (2014), and Past Lives (2023).",
        "[Demo Mode] Superb espionage and spy thrillers: Mission: Impossible - Fallout (2018), Casino Royale (2006), and Bridge of Spies (2015).",
        "[Demo Mode] Iconic gangster cinema: The Godfather Part II (1974), Scarface (1983), and Casino (1995).",
        "[Demo Mode] Heartwarming coming-of-age films: Lady Bird (2017), The Perks of Being a Wallflower (2012), and Boyhood (2014).",
        "[Demo Mode] High-stakes racing & sports dramas: Ford v Ferrari (2019), Rush (2013), and Moneyball (2011).",
        "[Demo Mode] Unsettling apocalyptic thrillers: A Quiet Place: Day One (2024), 28 Days Later (2002), and I Am Legend (2007).",
        "[Demo Mode] Fantasy epics you can rewatch forever: The Lord of the Rings: The Fellowship of the Ring (2001) and Harry Potter and the Prisoner of Azkaban (2004).",
        "[Demo Mode] Mind-bending Christopher Nolan essentials: Inception (2010), The Prestige (2006), and Tenet (2020).",
        "[Demo Mode] Top cyber-thrillers and hacker movies: The Matrix (1999), Minority Report (2002), and Upgrade (2018).",
        "[Demo Mode] Atmospheric mystery cinema: Wind River (2017), Mystic River (2003), and The Girl with the Dragon Tattoo (2011).",
        "[Demo Mode] Quirky dark comedies: In Bruges (2008), Three Billboards Outside Ebbing, Missouri (2017), and Fargo (1996).",
        "[Demo Mode] Ensemble whodunit picks: Glass Onion (2022), Murder on the Orient Express (2017), and Clue (1985).",
        "[Demo Mode] Action-packed comic book epics: The Dark Knight (2008), Avengers: Infinity War (2018), and The Batman (2022).",
        "[Demo Mode] Unforgettable musical films: The Greatest Showman (2017), Tick, Tick... Boom! (2021), and Bohemian Rhapsody (2018).",
        "[Demo Mode] Gripping historical epics: Gladiator (2000), Braveheart (1995), and Kingdom of Heaven (2005).",
        "[Demo Mode] Deep emotional tearjerkers: Manchester by the Sea (2016), The Green Mile (1999), and Schindler's List (1993).",
        "[Demo Mode] Thrilling survival in the wild: Into the Wild (2007), Cast Away (2000), and Everest (2015).",
        "[Demo Mode] Cult sci-fi favorites: The Fifth Element (1997), District 9 (2009), and Total Recall (1990).",
        "[Demo Mode] Chilling stalker & home invasion movies: Don't Breathe (2016), Panic Room (2002), and The Invisible Man (2020).",
        "[Demo Mode] Sizzling romantic chemistry: Crazy, Stupid, Love (2011), Pride & Prejudice (2005), and The Notebook (2004).",
        "[Demo Mode] Intense psychological character studies: Joker (2019), Black Swan (2010), and Taxi Driver (1976).",
        "[Demo Mode] Great westerns: Django Unchained (2012), Hell or High Water (2016), and True Grit (2010).",
        "[Demo Mode] Submarine & confined space thrillers: Das Boot (1981), Crimson Tide (1995), and The Hunt for Red October (1990).",
        "[Demo Mode] Fast cars and undercover agents: Fast Five (2011), Point Break (1991), and Ronin (1998).",
        "[Demo Mode] Gripping medical & virus thrillers: Contagion (2011), Outbreak (1995), and 12 Monkeys (1995).",
        "[Demo Mode] Iconic animation for anime lovers: Your Name (2016), Princess Mononoke (1997), and Akira (1988).",
        "[Demo Mode] Ultimate superhero origin stories: Iron Man (2008), Spider-Man (2002), and Batman Begins (2005)."
    )

    fun sendMessage(userText: String) {
        val trimmed = userText.trim()
        if (trimmed.isEmpty() || _isGenerating.value) return

        val userMessage = ChatMessage(text = trimmed, isUser = true)
        _messages.value = _messages.value + userMessage
        _isGenerating.value = true

        viewModelScope.launch(Dispatchers.Default) {
            delay(1200L) // Simulates realistic thinking time

            val randomReply = dummyResponses.random()
            _messages.value = _messages.value + ChatMessage(text = randomReply, isUser = false)
            _isGenerating.value = false
        }
    }

    fun clearChat() {
        _messages.value = listOf(
            ChatMessage(
                text = "Chat reset! (Under Development - Simulated Mode). What movie vibe are you looking for?",
                isUser = false
            )
        )
    }
}