import urllib2


class Transaction(object):
    def __init__(self):
        with open('GetCityForecastByZIP.xml') as f:
            self.soap_body = f.read()
    
    def run(self):
        req = urllib2.Request(url='http://wsf.cdyne.com/WeatherWS/Weather.asmx', data=self.soap_body)
        req.add_header('Content-Type', 'text/xml')
        resp = urllib2.urlopen(req)
        content = resp.read()