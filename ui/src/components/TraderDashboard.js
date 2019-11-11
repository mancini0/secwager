import React from 'react';
import Header from './Header';
import LeagueMenu from './LeagueMenu';
import DepthBook from './DepthBook';
import OrderEntry from './OrderEntry';
import About from './About';



class TraderDashboard extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      selectedInstrument: undefined
    };
  }


  handleInstrumentSelection = (selectedInstrument) => {
    this.setState({ selectedInstrument });
  }


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
      </div>

      < div className='row' style={{
        "backgroundColor": "#C8A2C8"
      }}>
        <About />
        {this.state.selectedInstrument &&
          <OrderEntry instrument={this.state.selectedInstrument} />
        }
      </ div>
    </React.Fragment>
  );

}

export default TraderDashboard;