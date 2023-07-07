notes on the design of fyscraper

# purpose #

fyscraper produces html files using yay/nay keyword lists and finance.yahoo (fy) equities descriptions:


| :symbol | :company     | :yayk | :nayk | :profile                              |
|:--------|:-------------|------:|------:|:-------------------------------------:|
| XYZ     | XYZ Products |    25 |     5 | This is an interesting organization.  |
| ABC     | A BC group   |     5 |    10 | Truly amazing items to be found here. |


yayk and nayk are the *cummulative* yays/nays count in the description.
the rationale for making it cummulative is to focus on density of appearence not just an appearence.

by default, rows are sorted by yayk to have good items float to the top. however, since the final product, which is an html file, can be read by spreadsheet software, the user is free to sort in any desired and unholy fashion befitting the individual's soul.

# overview #

there are 2 parts:

1. symbols -> tsv file
ticker symbols from various exchange text files are used acquire the company name and profile through finance.yahoo website. these are gathered in a dataset and then written out as tsv files.  

2. tsv -> html file
the tsv files are read into dataset consisting of columns :symbol, :company and :profile.  
ays files are read and turned into lists.  
the lists are matched against the profile to count frequency of occurences of the ay keywords in the :profile and create count columns :yayk, :nayk and :profileC (which color codes the keywords in html).  
the rows are sorted by :yayk.
finally, :symbol :company :yayk :nayk :profileC are written to exch.html file.

exch.tsv files are produced with columns "symbol" "company" "profile" from fy.
the actual mechanism for their production is unclear at present (see tsv-make below).

these exch.tsv files are used with the ay lists to colorcode keywords in the profile and produces the density of keywords.

# namespaces #

## io ##

**API**  
exchs, yays, nays  
exch-tsv>ds  
html-write

**dir** < "resources/"

**file>list [fnam]** < exch.txt yay.txt nay.txt
>
yay nay exchs

**tsv-trim [exch]** < exch.tsv
rewrites exch.tsv to "symbol" "company" "profile"
require only once to alter old items
>
exch.tsv

**tsv-make [exch]** < exch.txt??
TODO
update exch.tsv with new profile and new equities
not sure how right now
>
exch.tsv

**exch-txt>ds [fnam]** < exch.txt
rename old exch.txt file columns Symbol>:symbol Description>:company
likely will not require
>
exch.tsv

**exch-tsv>ds [exch]** < exch
makes ds from exch.tsv file
>
ds

**html-write [exch htmlpage]** < exch htmlpage(from hiccup)
spits out the htmlpage to html file
>
exch.html

