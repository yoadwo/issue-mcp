docker run -d -p 55000:1080 --name mockserver mockserver/mockserver:5.15.0
sleep 5
curl --location --request PUT 'http://localhost:55000/mockserver/expectation' \
--header 'Content-Type: application/json' \
--data '{
  "httpRequest" : {
    "method" : "POST",
    "path" : "/rest/api/latest/issue",
  },
  "httpResponse" : {
    "body" : "{\r\n  \"id\": \"10000\",\r\n  \"key\": \"ED-24\",\r\n  \"self\": \"https:\/\/your-domain.atlassian.net\/rest\/api\/3\/issue\/10000\",\r\n}"
  }
}'