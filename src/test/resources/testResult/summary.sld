<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc"
  xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:gml="http://www.opengis.net/gml"
  xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd">
  <NamedLayer>
    <Name>qw_portal_map:states_all</Name>
    <UserStyle>
      <Name>sample_density</Name>
      <Title>Discrete Sample Density</Title>
      <Abstract>A filter that filters all sample data contained in the waterquality portal for 
        the United States into five categories of discrete sample density, represented in different colors</Abstract>
      <FeatureTypeStyle>
         <Rule>
              <Title>0</Title>
              <ogc:Filter>
                  <ogc:PropertyIsLessThan>
                      <ogc:PropertyName>discrete_sample_count</ogc:PropertyName>
                      <ogc:Literal>1</ogc:Literal>
                  </ogc:PropertyIsLessThan>
              </ogc:Filter>
             <PolygonSymbolizer>
                 <Fill>
                    <!-- CssParameters allowed are fill (the color) and fill-opacity -->
                    <CssParameter name="fill">#FFFFFF</CssParameter>
                    <CssParameter name="fill-opacity">1</CssParameter>
                 </Fill>
                 <Stroke>
                    <CssParameter name="stroke">#000000</CssParameter>
                    <CssParameter name="stroke-width">0.2</CssParameter>
                 </Stroke>
              </PolygonSymbolizer>
          </Rule>
      	        <Rule>
          <Title>6 - 6</Title>
          <ogc:Filter>
            <ogc:PropertyIsBetween>
             <ogc:PropertyName>discrete_sample_count</ogc:PropertyName>
            <ogc:LowerBoundary>
                <ogc:Literal>6</ogc:Literal>
              </ogc:LowerBoundary>
              <ogc:UpperBoundary>
                <ogc:Literal>6</ogc:Literal>
              </ogc:UpperBoundary>
            </ogc:PropertyIsBetween>
          </ogc:Filter>
          <PolygonSymbolizer>
             <Fill>
                <!-- CssParameters allowed are fill (the color) and fill-opacity -->
                <CssParameter name="fill">#F3C391</CssParameter>
                <CssParameter name="fill-opacity">1</CssParameter>
             </Fill>
             <Stroke>
                <CssParameter name="stroke">#000000</CssParameter>
                <CssParameter name="stroke-width">0.2</CssParameter>
             </Stroke>
          </PolygonSymbolizer>
        </Rule>
                <Rule>
          <Title>7 - 49</Title>
          <ogc:Filter>
            <ogc:PropertyIsBetween>
             <ogc:PropertyName>discrete_sample_count</ogc:PropertyName>
            <ogc:LowerBoundary>
                <ogc:Literal>7</ogc:Literal>
              </ogc:LowerBoundary>
              <ogc:UpperBoundary>
                <ogc:Literal>49</ogc:Literal>
              </ogc:UpperBoundary>
            </ogc:PropertyIsBetween>
          </ogc:Filter>
          <PolygonSymbolizer>
             <Fill>
                <!-- CssParameters allowed are fill (the color) and fill-opacity -->
                <CssParameter name="fill">#F6A061</CssParameter>
                <CssParameter name="fill-opacity">1</CssParameter>
             </Fill>
             <Stroke>
                <CssParameter name="stroke">#000000</CssParameter>
                <CssParameter name="stroke-width">0.2</CssParameter>
             </Stroke>
          </PolygonSymbolizer>
        </Rule>
                <Rule>
          <Title>50 - 477</Title>
          <ogc:Filter>
            <ogc:PropertyIsBetween>
             <ogc:PropertyName>discrete_sample_count</ogc:PropertyName>
            <ogc:LowerBoundary>
                <ogc:Literal>50</ogc:Literal>
              </ogc:LowerBoundary>
              <ogc:UpperBoundary>
                <ogc:Literal>477</ogc:Literal>
              </ogc:UpperBoundary>
            </ogc:PropertyIsBetween>
          </ogc:Filter>
          <PolygonSymbolizer>
             <Fill>
                <!-- CssParameters allowed are fill (the color) and fill-opacity -->
                <CssParameter name="fill">#F07F45</CssParameter>
                <CssParameter name="fill-opacity">1</CssParameter>
             </Fill>
             <Stroke>
                <CssParameter name="stroke">#000000</CssParameter>
                <CssParameter name="stroke-width">0.2</CssParameter>
             </Stroke>
          </PolygonSymbolizer>
        </Rule>
                <Rule>
          <Title>478 - 503</Title>
          <ogc:Filter>
            <ogc:PropertyIsBetween>
             <ogc:PropertyName>discrete_sample_count</ogc:PropertyName>
            <ogc:LowerBoundary>
                <ogc:Literal>478</ogc:Literal>
              </ogc:LowerBoundary>
              <ogc:UpperBoundary>
                <ogc:Literal>503</ogc:Literal>
              </ogc:UpperBoundary>
            </ogc:PropertyIsBetween>
          </ogc:Filter>
          <PolygonSymbolizer>
             <Fill>
                <!-- CssParameters allowed are fill (the color) and fill-opacity -->
                <CssParameter name="fill">#E36122</CssParameter>
                <CssParameter name="fill-opacity">1</CssParameter>
             </Fill>
             <Stroke>
                <CssParameter name="stroke">#000000</CssParameter>
                <CssParameter name="stroke-width">0.2</CssParameter>
             </Stroke>
          </PolygonSymbolizer>
        </Rule>
                <Rule>
          <Title>504 - 676</Title>
          <ogc:Filter>
            <ogc:PropertyIsBetween>
             <ogc:PropertyName>discrete_sample_count</ogc:PropertyName>
            <ogc:LowerBoundary>
                <ogc:Literal>504</ogc:Literal>
              </ogc:LowerBoundary>
              <ogc:UpperBoundary>
                <ogc:Literal>676</ogc:Literal>
              </ogc:UpperBoundary>
            </ogc:PropertyIsBetween>
          </ogc:Filter>
          <PolygonSymbolizer>
             <Fill>
                <!-- CssParameters allowed are fill (the color) and fill-opacity -->
                <CssParameter name="fill">#D23F11</CssParameter>
                <CssParameter name="fill-opacity">1</CssParameter>
             </Fill>
             <Stroke>
                <CssParameter name="stroke">#000000</CssParameter>
                <CssParameter name="stroke-width">0.2</CssParameter>
             </Stroke>
          </PolygonSymbolizer>
        </Rule>
        
     </FeatureTypeStyle>
    </UserStyle>
    </NamedLayer>
</StyledLayerDescriptor>