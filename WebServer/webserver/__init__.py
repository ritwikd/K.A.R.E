from pyramid.config import Configurator
from cherrypy.wsgiserver import CherryPyWSGIServer
from pyramid.paster import get_app
import sys


def main(global_config, **settings):
    config = Configurator()
    config.include('pyramid_mako')

    config.add_static_view('assets', 'static/assets')
    config.add_static_view('css', 'static/css')
    config.add_static_view('js', 'static/js')

    config.add_route('home', '/')
    config.add_route('index', '/index')
    config.add_route('about', '/about')
    config.add_route('results', '/search/{owner}/{repo}')
    config.add_route('autocomplete', '/auto/{query}')
    config.add_route('404', '/404')
    config.add_route('statistics', '/statistics')

    config.scan()
    return config.make_wsgi_app()