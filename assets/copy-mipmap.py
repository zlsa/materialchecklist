#!/bin/env python3

import os

DENSITIES = {
    'ldpi': 0.75,
    'mdpi': 1.0,
    'hdpi': 1.5,
    'xhdpi': 2.0,
    'xxhdpi': 3.0,
    'xxxhdpi': 4.0
}

def export_icon(icon_path, icon_name, density=True, filename=None, quiet=False):
    svg_filename = filename
    
    if not svg_filename:
        svg_filename = './' + icon_path + '.svg'

    if density == True:
        print('exporting ' + icon_name + ' (' + svg_filename + ')')
        
        for d in DENSITIES:
            if not export_icon(icon_path, icon_name, d, svg_filename, True):
                return
            
        return

    mipmap_filename = os.path.join('../app/src/main/res/mipmap-' + density, icon_name + '.png')
    dpi = DENSITIES[density] * 90

    try:
        svg_time = os.path.getmtime(svg_filename)
        mipmap_time = os.path.getmtime(mipmap_filename)

        if mipmap_time > svg_time:
            if not quiet:
                print('skipping ' + icon_name + ' (mipmap newer than svg)')
            return False
    except FileNotFoundError:
        pass

    if not quiet:
        print('exporting ' + icon_name + ' (' + density + ') at ' + str(dpi) + ' dpi' + ' (' + svg_filename + ' => ' + mipmap_filename + ')')

    os.makedirs(os.path.dirname(mipmap_filename), exist_ok=True)
        
    command = 'inkscape ' + svg_filename + ' --export-png=' + mipmap_filename + ' --export-dpi=' + str(dpi) + ' > /dev/null'

    os.system(command)

    return True

def export_icons(icons):
    for i in icons:
        export_icon(i[1], i[0])

if __name__ == '__main__':
    export_icons([
        ['ic_launcher', 'app/ic_launcher'],
        ['ic_launcher_round', 'app/ic_launcher_round'],
        ['ic_launcher_foreground', 'app/ic_launcher_foreground']
    ])
