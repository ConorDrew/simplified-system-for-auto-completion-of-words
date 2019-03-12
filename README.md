# Simplified-System-For-Auto-Completion-of-Words
A data structure project for developing a simple system for completing words.

The project was to create a data structure for a simplified system for an autocompletion system, 
using a Trie data structure that contains 26 nodes (1 for each letter in the alphabet). <br/>
if the node has future characters coming off it, there will contain a future 26 nodes,

For example the word three words CAT, BAT, CAR

      Base Node 
      |     |
      B     C
      |     |
      A     A
      |     |\
      T     T R

The first step of the program is to read in a text document stemming and storing the words into a dictionary
and the frequency of each word. <br/>
The next is to store this dictionary into the Tri-Data structure. <br/>
The step after is to traverse the tri-data structure using recursion to do breadth and depth search.<br/>
The auto-complete section takes in a minimum of 1 character and will show the words that come after it, 
using the example from above if 'B' is entered only BAT is returned.
