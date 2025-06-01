@echo 윈도우용 실행 스크립트
java --add-exports java.desktop/com.apple.eawt=ALL-UNNAMED -jar fakeSMTP-2.0.jar -p 2525 -s -m
