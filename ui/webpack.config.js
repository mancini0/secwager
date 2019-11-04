const path = require('path');
const UglifyJsPlugin = require('uglifyjs-webpack-plugin');
const Dotenv = require('dotenv-webpack');
const fs = require('fs');

module.exports = (env) => {
    const currentPath = path.join(__dirname);
    const basePath = currentPath + '/.env';
    const envPath = basePath + '.' + env;
    const finalPath = fs.existsSync(envPath) ? envPath : basePath;
    console.log('path is: ' + finalPath);

    return (

        {
            entry: './src/main.js',
            output: {
                path: path.join(__dirname, 'public'),
                filename: 'bundle.js'
            },
            module: {
                rules: [
                    {
                        loader: 'babel-loader',
                        test: /\.js$/,
                        exclude: /node_modules/
                    }, {
                        test: /\.css$/,
                        use: ['style-loader', 'css-loader']
                    }
                ]
            },

            devtool: env !== 'production' ? 'cheap-module-eval-source-map' : false,

            optimization: {
                minimizer: [new UglifyJsPlugin()],
            },

            devServer: {
                contentBase: path.join(__dirname, 'public'),
                historyApiFallback: true
            },
            plugins: [new Dotenv({ path: finalPath })]
        });
};