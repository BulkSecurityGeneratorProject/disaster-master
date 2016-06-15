'use strict';

describe('Controller Tests', function() {

    describe('Mission Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMission, MockMissionStatus;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMission = jasmine.createSpy('MockMission');
            MockMissionStatus = jasmine.createSpy('MockMissionStatus');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Mission': MockMission,
                'MissionStatus': MockMissionStatus
            };
            createController = function() {
                $injector.get('$controller')("MissionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'jhipsterApp:missionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
