         IRC Poker Logs (Texas Hold'em, Omaha/8 and 7-Stud/8)
         ====================================================

About seven years of play on the IRC poker channels has been recorded and is
presented here in a tabular format.  The database contains over ten million
hands.  The entire play of each hand is recorded including the hole cards
exposed at the showdown; unfortunately, the hole cards of folded hands are not
available.

The database contains the record of the following IRC channels:

#7stud          7-card stud stud high/low (8 or better)
#7studhi        7-card stud high
#botsonly       10-20 Holdem (reserved for bots)
#h1-nobots      10-20 Holdem (reserved for humans)
#holdem         10-20 holdem (the original channel)
#holdemii       10-20 holdem
#holdem1        10-20 holdem
#holdem2        20-40 holdem ($2k minimum buyin)
#holdem3        50-100 holdem ($5k minimum buyin)
#holdempot      Pot-limit holdem
#nolimit        No-limit Holdem
#ohlpot         Pot-limit omaha high/low (8 or better)
#omaha          10-20 Omaha high/low (8 or better)
#omahahi        Omaha high only
#omahapot       Pot-limit omaha high only
#ptourney       Pot-limit omaha tournaments
#tourney        No-limit holdem tournaments

Anybody could join IRC and get 1000 in chips for free, but some channels had
minimum buyins greater than 1000.  Pplayers had to win chips by playing on the
smaller limit tables to be able to play on the higher limit tables.  This
scarcity of chips made them valuable and encouraged people to play their best
game.

Each database contains a file 'hdb' which summarizes each hand in a single
line, a file 'hroster' that records the list of players dealt in on each hand,
and a file 'pdb.<name>' for each player that summarizes that player's actions
during each hand.  The corresponding lines in each file all have the same
'timestamp', a large integer that uniquely identifies the hand in question.

An example hand is shown below, extracted from the #holdem3 database.  Every
file was searched for the timestamp 766303976; the matching player lines were
sorted into the order in which they acted in the betting.  A complete
description of each column is given at the end of this document.
                                   
HAND INFORMATION (in hdb file)
------------------------------
timestamp      hand #     #players/starting potsize
         dealer    #play flop    turn    river  showdn     board
766303976   1   455  8  6/600   6/1200  6/1800  3/2400  3s Jc Qd 5c Ah

ROSTER INFORMATION (in hroster file)
------------------------------------
766303976  8  Marzon spiney doublebag neoncap maurer andrea zorak justin

PLAYER INFORMATION (in pdb.* files)
-----------------------------------
player             #play prflop    turn         bankroll    winnings
          timestamp    pos   flop       river          action     cards
Marzon    766303976  8  1 Bc  bc    kc    kf      12653  300    0 
spiney    766303976  8  2 Bc  cc    kc    f       10237  300    0 
doublebag 766303976  8  3 cc  r     b     bc       7842  500    0 Jh Qh
neoncap   766303976  8  4 f   -     -     -        7857    0    0 
maurer    766303976  8  5 f   -     -     -       12711    0    0 
andrea    766303976  8  6 cc  c     c     f        7190  300    0 
zorak     766303976  8  7 r   c     c     cc       4460  500    0 As Kc
justin    766303976  8  8 c   c     c     r        4304  500 2400 Ad Qs


In English, the database entry says that 8 players are at the table; Marzon
makes a small blind (B), spiney makes a big blind (B), doublebag calls (c),
neoncap folds (f), maurer folds (f), andrea calls (c), zorak raises (r),
justin calls (c), Marzon calls (c), spiney calls (c), and finally andrea calls
(c) [no respect for zorak].  The pot is $600 and we have 6 players.  The flop
comes 3s Jc Qd, Marzon bets (b), spiney calls (c), doublebag raises (r) with
two pair, etc.  On the turn, Marzon and spiney check (k) to double bag, who
bets, etc.  On the river, justin catches an ace to make aces-up, raises and
wins the $2400 pot.  Note that we don't know the cards of those who folded
before the showdown.  Also note that the format does not record exact bet
amounts in pot-limit or no-limit, though they can often be inferred from the
total pot size after each round.


The following is a column-by-column description of the data files' format:

HDB format
==========
column 1        timestamp (supposed to be unique integer)
column 2        game set # (incremented when column 3 resets)
column 3        game # reported by dealer bot
column 4        number of players dealt cards
column 5        number of players who see the flop
column 6        pot size at beginning of flop
column 7        number of players who see the flop
column 8        pot size at beginning of turn
column 9        number of players who see the flop
column 10       pot size at beginning of river
column 11       number of players who see the flop
column 12       pot size at showdown
column 13+      cards on board (0, 3, 4 or 5)

HROSTER format
==============
column 1        timestamp
column 2        number of player dealt cards
column 3+       player nicknames


PDB format
==========
column 1        player nickname
column 2        timestamp of this hand (see HDB)
column 3        number of player dealt cards
column 4        position of player (starting at 1, in order of cards received)
column 5        betting action preflop (see below)
column 6        betting action on flop (see below)
column 7        betting action on turn (see below)
column 8        betting action on river (see below)
column 9        player's bankroll at start of hand
column 10       total action of player during hand
column 11       amount of pot won by player
column 12+      pocket cards of player (if revealed at showdown)


The betting action is encoded with a single character for each action:

        -       no action; player is no longer contesting pot
        B       blind bet
        f       fold
        k       check
        b       bet
        c       call
        r       raise
        A       all-in
        Q       quits game
        K       kicked from game
