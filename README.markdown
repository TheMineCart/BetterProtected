Setting this up as an Intellij project should be straight forward and pretty easy...
However, the tricky part comes when you want to run this plugin with an instance of CraftBukkit (Minecraft Server Mod).

  * Building the project structure...
    1. Make a "minecraft" directory wherever you like.
    2. Download "craftbukkit-1.0.1-R1.jar" from the following url and place it into your newly created "minecraft" folder:
        - http://repo.bukkit.org/service/local/artifact/maven/redirect?g=org.bukkit&a=craftbukkit&v=RELEASE&r=releases
    3. Now you need a "plugins" folder in the "minecraft" directory.
    4. Navigate into your new "plugins" folder.
    5. Clone the git repository into that directory

        git clone git@github.com:TheMineCart/BetterProtected.git
        
  * Setting up Intellij...
    1. First open up the Project Structure dialogue (ctrl + alt + shift + s).
    2. Add/Select a java sdk. It should be 1.6.0_20 or earlier in order to work with servers running in older JREs.
    3. Under "Project Settings" on the left hand side, select "libraries".
    4. Create a new "Project Library" with the + button at the top.
        - Select "java" from the drop down "New Project Library".
        - Navigate to and select the "craftbukkit-1.0.1-R1.jar" file that we added earlier.
        - In the next window "Choose Modules", select "BetterProtected" and press OK.
    5. Under "Project Settings" on the left hand side, select "artifacts".
    6. Create a new "Artifact" by pressing the + button at the top.
        - Select "jar" -> "Modules with Dependencies" from the drop down "New".
        - Press OK with the default selections.
        - Change the output directory to be "minecraft/plugins".
        - Under "Output Layout" add a "file" and select plugin.yml from the project directory.
    7. Save the configuration by hitting Apply/OK and you should be good to go.