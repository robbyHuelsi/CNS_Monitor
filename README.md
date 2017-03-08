# CNS_Monitor

Central-Nervous-System Monitor

Liegt auf github: https://github.com/visio/CNS_Monitor

Dieses Programm ist dafür zuständig:
- Die Rechner (zur Zeit: NUC1, NUC2, MB) im lokalen Netzwerk nach ihrer Mac Adresse im Netzwerk zu suchen und ihre IP Adressen speichern.
- Die Module (zum Beispiel TTS, STT,... )zu starten und zu überwachen

Dafür bietet es:
- Eine Config-Datei (src/config.json) in der alle Rechner (mit Mac Adressen und Benutzern) und alle Module (mit Rechner auf dem es läuft, Port auf dem es hört und Startcommand) hinterlegt sind
- Eine GUI in der alle Rechner mit gefundener IP adresse angezeigt werden, alle Module gestartet werden können (über SSH auf allen Rechnern) und der output der Module angezeigt werden kann.
