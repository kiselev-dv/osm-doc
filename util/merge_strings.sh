#!/bin/bash

DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

for lang in en ru
do
    cat "$DIR/../l10n/hierarchy_$lang.properties" "$DIR/../l10n/feature_$lang.properties" "$DIR/../l10n/feature_tags_$lang.properties" "$DIR/../l10n/trait_$lang.properties" > "$DIR/../l10n/strings_$lang.properties"
done
