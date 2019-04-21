This repository contains the partial source code for an Unreal Package
Obfuscation/Editing tool I wrote in 2008. I stripped out the obfuscation
functionality as it only really makes sense to obfuscate security tools such as
anti-cheats.

What people might find useful is that this tool can fix UPackages that were
intentionally (?) broken so they only run on consoles. Passing fixNodes=true to
this tool will make it repair such packages.