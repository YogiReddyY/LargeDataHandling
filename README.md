Generate PDF Report Flow:

1) Retrieve Data from Database. Done
2) Create the xml with retrieved data. Done
3) By using the count of available records, developing an algorithem which divides the data into multiple pdfs. Done
4) Using FOP(Formatting Objects Processor) to convert the xml into PDF. Done
5) Merge all the pdfs into one. POC Done


Notes : Single Threaded model,
          A) Convert XML into PDF: 50K-3Mins average.
		  B) Merging PDF Files : 25 Files, 3MB each, taken 20sec.

12-03-2019: Planned to work on
1) Word Wrap
2) Applyining Multithreading
3) Performance Tuning