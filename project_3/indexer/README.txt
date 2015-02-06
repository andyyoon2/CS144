Team: Last Minute

We will create a single index over the the concatenation of the name, categories, and description for each item.
We choose to do this because the spec says that our searches will be done under the union of these fields;
so, it only makes sense to combine them for the purposes of indexing.
Additionally, separation of these fields into separate indexes would make multiword queries much harder because the
OR of multiple keywords existing in a document must now be facilitated by our code when the index searcher is
already made to handle this.

