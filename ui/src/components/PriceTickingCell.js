import React from 'react';
import '../styles/PriceTickingCell.css';
class PriceTickingCell extends React.Component {

    constructor(props) {
        super(props)
        this.state = { price: undefined, arrow: undefined, tickClass:'waiting' }
    }

    static getDerivedStateFromProps(newProps, oldState) {
        if (newProps.price && oldState.price && newProps.price !== oldState.price) {
            const upArrow = '\u21E7'
            const downArrow = '\u21E9'
            const change = (newProps.price-oldState.price).toFixed(2)
            if (newProps.price > oldState.price) {
                return { price: newProps.price, arrow: upArrow, bgColor: 'green', change, tickClass:'fadeout'}
            }
            else {
                return { price: newProps.price, arrow: downArrow, bgColor: 'red', change, tickClass:'fadeout' }
            }
        }
        return { price: newProps.price, arrow: undefined, tickClass: 'waiting', change:undefined}
    }

    componentDidUpdate(prevProps, prevState, snapshot){
        if(this.state.tickClass === 'fadeout'){
            setTimeout(()=>this.setState({tickClass:'waiting'}),2000)
        }
    }



    render(props) {
        return (
            <div>
                {this.state.arrow &&
                    <span className={this.state.tickClass} style={{
                        backgroundColor: this.state.bgColor,
                    }}>{this.state.arrow} {this.state.change}</span>

                }
                {this.state.price? '  '+this.state.price : 'waiting for first price...'}
            </div>
        );

    }

}




export default PriceTickingCell