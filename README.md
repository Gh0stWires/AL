#AL

Anyone remember brillo? No? It's ok neither does google. Brillo was google's first attempt at internet of things. I don't really remember what the brillo os was supposed to do but I guess it doesn't really matter as I can't even find that many remnants of it online. Android Things is google's new entry into internet of things. I really like this approach because it is literally just android with out a launcher. Why they didn't do this sooner I have no idea. Best of all it runs on a raspberry pi. Why am I telling you all this? Well AL is just an android things app running on a raspberry pi. I have been wanting to play around android things for a while but I didn't really have anything I could use it for till now. 

AL is a very simple android app. It literally is just one activity with a text view at the center. Because android things is just android it allowed me to add firebase to AL. The user of AL signs into there account that is setup via firebase. Each user in the firebase database has one entry, which is there most recent command. AL listens for changes to the users command data in firebase. When a change is detected AL grabs that new string and puts it in its text view and then sends that string to tj for handling. If you are wondering how the data get put into firebase in the first place, that is up next when I talk about JD. 

I plan on adding more functionality to AL in the future. Here is a few ideas I have.

* News Feed
* Notifications Feed
* Google Cast for Chromecast
