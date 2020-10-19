import requests
import argparse
import sys
import json

def findMyLuck(sign): 
    params = (('sign', sign),('day', 'today'),)
    reqPost = requests.post('https://aztro.sameerkumar.website/', params=params)
    #print(type(response))
    response = reqPost.json()
    try:
        return response['description']
    except:
        return "Please enter a horoscope sign from the list: aries, libra, leo, cancer, virgo, gemini, taurus, scorpio, pisces, aquarius, capricorn, sagittarius."
if __name__ == '__main__':
     parser = argparse.ArgumentParser(description="Process a user's sign.")
     parser.add_argument('-sign',  help='a sign is needed to find your horoscope')
     try:
        args = parser.parse_args()
        print(findMyLuck(args.sign))
     except:
        print("Please enter a horoscope sign from the list: aries, libra, leo, cancer, virgo, gemini, taurus, scorpio, pisces, aquarius, capricorn, sagittarius.")
    