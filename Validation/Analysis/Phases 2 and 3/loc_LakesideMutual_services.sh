#!/usr/bin/env bash

cloc --by-file --match-f="service_.*.services" --not-match-f="extracted.*" --force-lang-def="cloc_lemma_def" "../../Case Study Models and Code/Lakeside Mutual/Phase 2/extracted models/"
