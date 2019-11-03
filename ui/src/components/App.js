import React from 'react';
import Header from './Header';
import LeagueMenu from './LeagueMenu';
import DepthBook from './DepthBook';
import OrderEntry from './OrderEntry';



class App extends React.Component {

  constructor(props){
    super(props);
    this.state = {
      selectedInstrument: undefined
    };
  }


handleInstrumentSelection = (selectedInstrument) => this.setState({selectedInstrument});


render = (props) => (
  <React.Fragment>
    <div className='row' style={{
      "height": "50px"
    }}>
      <Header />
    </div>
    <div className='row' style={{
      "height": "250px"
    }}>
      < div className='col' style={{
        "backgroundColor": "lightgrey"
      }}>
        <LeagueMenu handleInstrumentSelection={this.handleInstrumentSelection} />
      </div>
    {this.state.selectedInstrument && <div className='col'>
      <OrderEntry instrument={this.state.selectedInstrument}/>
  </div> }
    </div>
  </React.Fragment>
);

}

export default App;