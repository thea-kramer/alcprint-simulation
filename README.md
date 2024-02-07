# alcprint_simulation
A data pipeline for analysing, simulating and detecting addiction craving situations. 

## Usage
The repository contains 6 notebooks that perform data analysis, detection, simulation and prediction on alcohol cravings from the Reddit forum "stopdrinking" (https://www.reddit.com/r/stopdrinking/). It also contains a java script that reconstructs survey data from the following paper: https://www.frontiersin.org/articles/10.3389/fpsyg.2019.00074/full 

The 6 notebooks should be executed in order and contain the following steps:

- **01_Reddit_Analyzer** Contains code for retrieving data from Reddit, the filter logic for cravings posts and various analysis of craving posts
- **02_Similarity_Analyzer** Calculates Gensim similarity scores for analysing semantic contexts of craving
- **03_CravingTopicModels** Divides cravings into 10 topics via an LDA algorithm
- **04_Attributes_Analyzer** Retrieves context words of cravings
- **05_Evaluator** Evaluates the Attribute Analyzer on a test set. The test set is provided in this repository in the Data folder as a compressed zip file containing the labeled test set CSV file.
- **06_Simulator** Experiments how predictable cravings would be based on a craving data simulation

## Retrieval of the Reddit Data
The original data used for this notebook was retrieved from the [stopdrinking Reddit forum](https://www.reddit.com/r/stopdrinking/) via the [Reddit PushShift API](https://github.com/pushshift/api). The latest information about how to retrieve Reddit data can be found under [the official Reddit API page](https://www.reddit.com/dev/api/). We used the data from April 2017 until April 2022 and retrieved it via the first code snippets in 01_Reddit_Analyzer.

## Test set
The test set can be found in the folder Data and was labeled by 3 human labellers (labels true or false). Each value 000,010,111,... represents a concatenation of labels of the three labellers, e.g. if the first labeler said true, the second false and the third true, the label would be 101. At the time of labelling the labellers did not see the others' answers.

## Background
Alcohol cravings are considered a major factor in relapse among individuals with alcohol use disorder (AUD). This study aims to investigate the frequency and triggers of cravings in the daily lives of people with alcohol-related issues. Large amounts of data are analyzed with Natural Language Processing methods to identify possible groupings and patterns. An advantage of Reddit serves as a valuable data source due to its non-clinical environment, allowing for a wide range of participants, including those who do not seek therapy or similar interventions. Due to the anonymity, the posts are likely phrased in a more intimate and authentic manner than in study settings. 

