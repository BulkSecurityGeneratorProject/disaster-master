'use strict';

describe('Controller Tests', function() {

    describe('Unit Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockUnit, MockFleet, MockVehicle;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockUnit = jasmine.createSpy('MockUnit');
            MockFleet = jasmine.createSpy('MockFleet');
            MockVehicle = jasmine.createSpy('MockVehicle');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Unit': MockUnit,
                'Fleet': MockFleet,
                'Vehicle': MockVehicle
            };
            createController = function() {
                $injector.get('$controller')("UnitDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jhipsterApp:unitUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
